import sideToggle from './sideToggle';
import { useState } from 'react';
import footerData from '../../Data/footerData';
import  {Link} from 'react-router-dom'
import postCommentInFeed from './comment';
import UpLoad from '../PostReg/upload';
import axios  from 'axios';
import { useEffect } from 'react';


function Main(){

const [foot, setFoot] = useState(footerData)
const [file, setFile] = useState('')
const [test, setTest] = useState('')
const [mainImg,setMainImg] = useState([])


const onLogout = () =>{
  // localStorage에 있는 item삭제
  localStorage.removeItem('user')
  localStorage.removeItem('token')
  document.location.href = '/'
}
// 이미지 받아오기
useEffect(()=>{
  axios.get('https://jsonplaceholder.typicode.com/photos?id=1&id=2&id=3&id=4')
  // axios.get('https://jsonplaceholder.typicode.com/photos?id=2')
  .then(res =>{
    setMainImg([...res.data])
  })
},[])


return (
<>
  {/* Header*/}
  <header className="header">
    <nav className="header__content">
      <div className="header__buttons">
        <p>Social Gallery</p>
      </div>

      <div className="header__buttons header__buttons--desktop">
        <a href="#">
          <img src="/assets/Main/home_btn.jpg" alt="" />
        </a>
        <a href="#">
          <img src="/assets/Main/msg_btn.png" alt="" />
        </a>
        <a href="#">
          <img src="/assets/Main/video_btn.png" alt="" />
        </a>
        <button className="profile-button">
          <div className="profile-button img">
            <Link to = "/login"><img src="/assets/main/user.png" alt="User Picture" /></Link>
          </div>
        </button>
      </div>
    </nav>
  </header>
  {/* Main Contents */}
  <main className="main-container">
    <section className="content-container">
      <div className="content">
        <div className="posts">
          {/* Content */}
          {
            mainImg.map((a,i)=><Content key={a.id}mainImg={mainImg} i={i}></Content>)
          } 
        </div>
      </div>
    </section>
  </main>


  {/* Side Menu */}
  <section className="side-menu">
    <div className="side-menu__title">
      <div className="side-menu__title_info">
        <a>일상 Gallery</a>
        <span></span>
      </div>
    </div>
    {/* Side text */}
    <div className="side-menu__title-section">
      <div className="side-menu__title-header">
        <h2>전체 게시글</h2>
        <button>+</button>
      </div>
      {/* Side Contents */}
      <SideContents></SideContents>

    </div>
  </section>
  {/* Footer */}
  <div className="side-menu__footer" >
    <div className="side-menu__footer_links" >
      <ul className="side-menu__footer_list" >
        {
        foot.map((a,i)=> <Footer key={a.id} foot={foot} i={i}></Footer> )
        }
      </ul>
    </div>
    <span className="side-menu__footer_copyright">
      &copy; 2022 Social Gallery
    </span>
  </div>

  {/* Sidebar --> */}
  <nav className="sidebar close" id="nav">
    <header>
      <div className="image-text">
        <span className="image">
          <img src="assets/Main/user.png" alt="" />
        </span>
        <div className="text logo-text">
          <span className="name">User1</span>
          <span className="profession">Welcome</span>
        </div>
      </div>
      <i className="bx bx-chevron-right toggle" id="close" onClick={sideToggle}></i>
    </header>


    <div className="menu-bar">
      <div className="menu">
        <li className="search-box">
          <i className="bx bx-search icon"></i>
          <input type="text" placeholder="Search..." />
        </li>



        <ul className="menu-links">
          <li className="nav-link">
            <a href="#">
              <i className="bx bx-home-alt icon"></i>
              <span className="text nav-text">Home</span>
            </a>
          </li>
          <li className="nav-link">
            <a href="#">
              <i className='bx bx-group icon'></i>
              <span className="text nav-text">Group</span>
            </a>
          </li>
          <li className="nav-link">
            <a href="#">
              <i className='bx bx-bell icon'></i>
              <span className="text nav-text">Notifications</span>
            </a>
          </li>
          <li className="nav-link">
            <a href="#">
              <i className='bx bx-news icon'></i>
              <span className="text nav-text">News</span>
            </a>
          </li>
          <li className="nav-link">
            <a href="#">
              <i className='bx bx-heart icon'></i>
              <span className="text nav-text">Likes</span>
            </a>
          </li>
          <li className="nav-link">
            <a href="#">
              <i className='bx bx-calendar-event icon'></i>
              <span className="text nav-text">Event</span>
            </a>
          </li>
          <li>
            <a href="#">
              <i className="bx bx-log-out icon"></i>
              <span className="text nav-text" onClick={onLogout}>Logout</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</>
)
}

function Content(props){
  const [comment, setComment] = useState(null)

  // 댓글
  const onCommentHandler = (e) =>{
    setComment(e.currentTarget.value)
  }
  const commentSubmit = () => {
    axios.post('',{
      comment : comment
    },{
      headers: {
        'Content-Type': 'application/json'
        }
    })
    .then(res => res.json())
    .then(result => console.log('결과: ', result));
  }

// id=1일 데이터 받아오기
// const Pid = props.mainImg.filter((a,i) =>{
//   return (a.id < 2)
// })


return (
<>
  <article className="post">
    <div className="post__header">
      <div className="post__profile">
        <a href="#" className="post__avatar">
          <img src="assets/Main/user.png" alt="User Picture" />
        </a>
        <span>user1</span>
        {/* <UpLoad></UpLoad> */}
        <UpLoad></UpLoad>
      </div>
    </div>
    <div className="post__content">
      <div className="post__medias" >
      {/* map */}
      <img src={props.mainImg[props.i].thumbnailUrl} alt="" />
      </div>
    </div>
    <div className="post__footer">
      <div className="post__buttons">
        <button className="post__button">
          <img src="assets/Main/chat_btn.png" alt="" />
        </button>
        <button className="post__button">
          <img src="assets/Main/love_btn.png" alt="" />
        </button>
        <button className="post__button post__button--align-right">
          <img src="assets/Main/more_btn.png" alt="" />
        </button>
      </div>
      <div className="post__infos">
        <div className="post__title">
          <span>{props.mainImg[props.i].title}</span>
        </div>
        <div className="post__description">
          <span>{props.mainImg[props.i].title}</span>
        </div>
        <div className="post__border">
        </div>
        {/*
        <!-- 댓글 --> */}
        <div>
          <ul className="comment_list">
            <li>
            </li>
          </ul>
        </div>
        <section className="post_comment_wrap">
          <input 
            id="post_comment_input" 
            type="text" 
            placeholder="댓글 달기..." 
            onChange={onCommentHandler}
            />
          <button 
            className="post_comment_btn"
            onClick={commentSubmit}
            >
            <i className='bx bx-send'></i>
          </button>
        </section>
        <span className="post__date-time">more</span>
      </div>
    </div>
  </article>
</>
)
}
function SideContents(){
return (
<>
  <div className="side-menu__title-content">
    <div className="side-menu__recommend"></div>
    <div className="side-menu__recommend_info">
      <a href="#">카페에 다녀오고..</a>
      <span>동해물과 백두산이 마르고</span>
    </div>
  </div>
</>
)
}

function Footer(props){
return (
<>
  <li className="side-menu__footer_item"  >
    <a className="side-menu__footer_link" href="#">{props.foot[props.i].text}</a>
  </li>
</>
)
}
export default Main;