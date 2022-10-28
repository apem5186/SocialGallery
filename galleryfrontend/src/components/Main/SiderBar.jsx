import sideToggle from './sideToggle';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useState } from 'react';

function Sidebar({setMainImg}){
    const [searchTitle, setSearchTitle] = useState([])

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
        axios.get(dev_url +`/v1/logout?uid=`+uid)
            .then(res=>{
                if(res.status === 200){
                    localStorage.removeItem('token')
                    localStorage.removeItem('user')
                    localStorage.removeItem('uid')
                    navigate('/')
                }
            })
    }

    //username

    // 검색
    const base_URL = "http://localhost:8080"
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

    const searchList = (e) => {
        axios.get(dev_url + '/api/post?keyword=' + searchTitle)
            .then(res => {
                setMainImg([...res.data.list])
            })
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
                            <span className="name">어서오세요</span>
                            <span className="profession">Welcome</span>
                        </div>
                    </div>
                    <i className="bx bx-chevron-right toggle" id="close" onClick={(e)=>{
                        // userProfile(e)
                        sideToggle(e)
                    }}></i>
                </header>


                <div className="menu-bar">
                    <div className="menu">
                        <li className="search-box">
                            <i className="bx bx-search icon"
                               onClick={searchList}></i>
                            <input
                                type="text"
                                placeholder="Search..."
                                onChange={(e)=> setSearchTitle(e.target.value)}
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