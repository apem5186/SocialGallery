import { useNavigate } from 'react-router-dom';
import SideToggle from "./SideToggle";
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setSearchImg } from '../../store/Store';
import { setPostAll } from '../../store/Store';

function Sidebar(){

    const searchTitle = useSelector((state)=>state.searchImg.searchList)
    const userData = useSelector((state)=>state.userData.userDataList)
    const isLogin = useSelector((state)=>state.isLogin.isLoginList)
    const dispatch = useDispatch()

    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"



    const navigate = useNavigate()

    // Uid
    let [uid, setUid] = useState('')

    const getUid = () => {
        uid = localStorage.getItem('uid')
        setUid(uid)
        return uid
    }

    // Logout
    const onLogout = () =>{
        getUid()
        axios.get(dev_url + `/v1/logout?uid=`+uid)
            .then(res=>{
                if(res.status === 200){
                    localStorage.removeItem('token')
                    localStorage.removeItem('user')
                    localStorage.removeItem('uid')
                    navigate('/')
                    alert('로그아웃 됐습니다.')
                    window.location.reload()
                }
            })
    }

    // 검색
    const base_URL = "http://localhost:8080"
    const params = new URLSearchParams(window.location.search);

    let category = params.get("category")

    const search1 = (e) => {
        axios.get(dev_url + '/api/post?keyword=' + searchTitle)
            .then(res => {
                dispatch(setPostAll([...res.data.list]))
                console.log()
            })
        return search1
    }
    const search2 = (e) => {
        axios.get(dev_url + '/api/post/category?category=' + category + "&keyword=" + searchTitle)
            .then(res => {
                dispatch(setPostAll([...res.data.list]))
            })
        return search2
    }

    return (
        <>
            <nav className="sidebar close" id="nav">
                <header>
                    <div className="image-text">
				<span className="image">
					<img src="/assets/Main/user.png" alt="" />
				</span>
                        <div className="text logo-text">
                            {/* username */}
                            <span className="name">
                                {
                                    isLogin === false
                                        ? <div>로그인을 해주세요.</div>
                                        :<>
                                            <span>{userData.username}님</span>
                                            <span className="profession">Welcome</span>
                                        </>

                                }
                            </span>
                        </div>
                    </div>
                    <i className="bx bx-chevron-right toggle" id="close" onClick={(e)=>{
                        SideToggle(e)
                    }}></i>
                </header>


                <div className="menu-bar">
                    <div className="menu">
                        <li className="search-box">
                            <i className="bx bx-search icon"
                               onClick = { category === null ? search1 : search2}></i>
                            <input
                                type="text"
                                placeholder="Search..."
                                onChange={e=> {
                                    dispatch(setSearchImg(e.target.value))
                                }}
                            />
                        </li>
                        <ul className="menu-links">
                            <li className="nav-link">
                                <Link to = "/">
                                    <i className="bx bx-home-alt icon"></i>
                                    <span className="text nav-text">Home</span>
                                </Link>
                            </li>
                            <li className="nav-link">
                                <Link to = "#">
                                    <i className='bx bx-group icon'></i>
                                    <span className="text nav-text">Group</span>
                                </Link>
                            </li>
                            <li className="nav-link">
                                <Link to ="#">
                                    <i className='bx bx-bell icon'></i>
                                    <span className="text nav-text">Notifications</span>
                                </Link>
                            </li>

                            <li className="nav-link">
                                <Link to = "#" >
                                    <i className='bx bx-news icon'></i>
                                    <span className="text nav-text">News</span>
                                </Link>
                            </li>
                            <li className="nav-link">
                                <Link to ="#">
                                    <i className='bx bx-heart icon'></i>
                                    <span className="text nav-text">Likes</span>
                                </Link>
                            </li>
                            <li className="nav-link">
                                <Link to ="#">
                                    <i className='bx bx-calendar-event icon'></i>
                                    <span className="text nav-text">Event</span>
                                </Link>
                            </li>
                            <li>
                                <div onClick={onLogout}>
                                    <i className="bx bx-log-out icon"></i>
                                    <span className="text nav-text">Logout</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </>
    )
}

export default Sidebar