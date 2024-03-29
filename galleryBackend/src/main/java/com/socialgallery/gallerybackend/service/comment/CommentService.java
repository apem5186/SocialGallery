package com.socialgallery.gallerybackend.service.comment;

import com.socialgallery.gallerybackend.advice.exception.CommentNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.comment.CommentRequestDTO;
import com.socialgallery.gallerybackend.dto.comment.CommentResponseDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.CommentRepository;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.security.SignService;
import com.socialgallery.gallerybackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final UsersService usersService;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final JwtProvider jwtProvider;

    private final SignService signService;
    
    // 댓글 생성 기능
    @Transactional
    public String commentSave(Long pid, CommentRequestDTO commentRequestDTO, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        // 게시물 정보 파라미터값으로 받아오기
        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        // 유저정보와 토큰이 정상적이면 / access 토큰 만료시 재발급
        if (checkToken(commentRequestDTO.getUsers().getId(), request, response)) {
            // 유저 정보 post 이용해서 받아오기
            Comment entity = commentRequestDTO.toEntity();
            entity.setPost(post);
            // 저장
            commentRepository.save(entity);
            return String.valueOf(entity.getCid());
        }
        return "댓글 생성 실패";
//        else {
//            return "redirect:http://elasticbeanstalk-ap-northeast-2-506714295105.s3-website.ap-northeast-2.amazonaws.com/login";
//        }

    }

    // 댓글 수정
    @Transactional
    public String update(Long cid, CommentRequestDTO commentRequestDTO, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        if (checkToken(commentRequestDTO.getUsers().getId(), request, response)) {
            // 댓글 정보 받아오기
            Optional<Comment> result = commentRepository.findById(cid);
            result.ifPresent(comment -> comment.update(commentRequestDTO.getComment()));
        }
        return String.valueOf(cid);
//        else {
//            return "redirect:http://elasticbeanstalk-ap-northeast-2-506714295105.s3-website.ap-northeast-2.amazonaws.com/login";
//        }
    }

    // 댓글 삭제
    @Transactional
    public String delete(Long cid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 게시물 정보 파라미터값으로 받아오기
        Comment comment = commentRepository.findById(cid).orElseThrow(CommentNotFoundCException::new);

        if (checkToken(comment.getUsers().getId(), request, response)) {
            commentRepository.deleteByCid(cid);
        }
            return String.valueOf(cid);
//        else {
//            return "redirect:http://elasticbeanstalk-ap-northeast-2-506714295105.s3-website.ap-northeast-2.amazonaws.com/login";
//        }
    }

    // 댓글 가져오기
    @Transactional
    public List<CommentResponseDTO> getListOfComment(Long pid) {

        Post post = Post.builder()
                .pid(pid).build();

        List<Comment> result =commentRepository.findByPost(post);

        return result.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public List<CommentResponseDTO> getAllOfComment() {
        List<Comment> result = commentRepository.findAll();

        return result.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }
    // TODO : 리프레시토큰 삭제, isLogin 변경까진 되나 로컬스토리지와 alert창, 로그인페이지 이동이 안됨
    // TODO : response.sendRedirect하면 네트워크 호출이 됨 컨트롤러 하나 만들어서 로그인페이지로 이동하는 코드 짜봐야할듯
    // TODO : api 호출하면 DELETE매핑으로 찍히는데 방법 찾아야함
    @Transactional
    public boolean checkToken(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Users users = userRepository.findById(id).orElseThrow(UserNotFoundCException::new);
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String accessToken = jwtProvider.resolveToken(request);
        log.info("ACCESSTOKEN : " + accessToken);
        String rtoken = refreshToken.orElseThrow().getToken();
        log.info("REFRESHTOKEN : " + rtoken);
        if (!jwtProvider.validationToken(rtoken)) {
            log.info("VALIDATIONTOKEN TEST REFRESHTOKEN");
            request.setAttribute("Authorization", "");
            signService.logout(id);
            return false;
        }
        if (!jwtProvider.validationToken(accessToken)) {
            TokenRequestDTO tokenRequestDTO = TokenRequestDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(rtoken)
                    .build();
            signService.reissue(tokenRequestDTO);
            return true;
        }
        return true;
    }
}
