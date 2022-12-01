package com.socialgallery.gallerybackend.service.user;

import com.socialgallery.gallerybackend.advice.exception.EmailLoginFailedCException;
import com.socialgallery.gallerybackend.advice.exception.EmailSignUpFailedCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserLoginResponseDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.security.SignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * @Reference https://ws-pace.tistory.com/72?category=964036
 * GET 요청에는 UserResponseDTO를 반환
 * POST PUT 요청에는 UserRequestDTO를 반환
 */

@Service
@Slf4j
@AllArgsConstructor
public class UsersService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RefreshTokenJpaRepo refreshTokenJpaRepo;

    private JwtProvider jwtProvider;

    private SignService signService;

    @Transactional(readOnly = true)
    public UserLoginResponseDTO login(String email, String password) {
        Users users = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedCException::new);
        if (!passwordEncoder.matches(password, users.getPassword()))
            throw new EmailLoginFailedCException();
        return new UserLoginResponseDTO(users);
    }

    @Transactional
    public Long signUp(UserSignUpRequestDTO userSignUpDTO) {
        if (userRepository.findByEmail(userSignUpDTO.getEmail()).orElse(null) == null)
            return userRepository.save(userSignUpDTO.toEntity()).getId();
        else throw new EmailSignUpFailedCException();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(users);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByUsername(String username) {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, UserRequestDTO userRequestDTO, HttpServletRequest request) {
        if (checkToken(id, request)) {
            Users modifiedUser = userRepository
                    .findById(id).orElseThrow(UserNotFoundCException::new);
            modifiedUser.updateUsername(userRequestDTO.getUsername());
        }

        return id;
    }
    
    // TODO : 삭제가 안됨 foreign key 때문에 안되는건지 checkToken을 써서 안되는건지는 확인을 해봐야 함
    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        //userRepository.deleteById(id);
        //log.info("유저 삭제 완료");
        if (checkToken(id, request)) {
            userRepository.deleteById(id);
            log.info("유저 삭제 완료");
        }

    }

    @Transactional
    public boolean checkToken(Long id, HttpServletRequest request) {

        Users users = userRepository.findById(id).orElseThrow(UserNotFoundCException::new);
        UserResponseDTO userPk = findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String accessToken = jwtProvider.resolveToken(request);
        log.info("ACCESSTOKEN : " + accessToken);
        String rtoken = refreshToken.orElseThrow().getToken();
        log.info("REFRESHTOKEN : " + rtoken);
        if (!jwtProvider.validationToken(rtoken)) {
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
