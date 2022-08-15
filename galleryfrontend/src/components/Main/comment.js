// 댓글
function postCommentInFeed(){
    window.onload=function(){
    const commentInput = document.getElementById('post_comment_input');
    const commentPostBtn = document.getElementsByClassName('post_comment_btn')[0];
  
    // 댓글 입력시 요소 생성
    const addNewComment = () => {
      const newCommentLocation = document.getElementsByClassName('comment_list')[0];
      const newComment = document.createElement('li');
  
      newComment.innerHTML = `
        <div class="user_desc">
          <em>User1</em>
          <span>${commentInput.value}</span>
        </div>
      `;
  
      newCommentLocation.appendChild(newComment);
      commentInput.value = '';
    }
  
  
    // 게시 버튼 활성화
    commentInput.addEventListener('keyup', () => {
      commentInput.value ? commentPostBtn.style.opacity = '1' : commentPostBtn.style.opacity = '.3';
      if (window.event.keyCode === 13 && commentInput.value) {
        addNewComment();
      }
    });
  
    // 댓글 게시
    commentPostBtn.addEventListener('click', () => {
      if (commentInput.value) {
        addNewComment();
      } else {
        alert('댓글을 입력해주세요.');
      }
    })
  }
}
postCommentInFeed();
export default postCommentInFeed;