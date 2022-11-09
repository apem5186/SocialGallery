import { Link } from "react-router-dom";

function MainHeader(){
    
    return(
        <>
            <header className="header">
                <nav className="header__content">
                    <div className="header__buttons">
                        <Link to ='/'><p onClick={()=>{
                            window.location.replace ('/')
                        }}>Social Gallery</p></Link>
                    </div>

                    <div className="header__buttons header__buttons--desktop">
                        <Link to="#">
                            <img src="/assets/Main/home_btn.png" alt="" />
                        </Link>
                        <Link to="#">
                            <img src="/assets/Main/msg_btn.png" alt="" />
                        </Link>
                        <Link to="#">
                            <img src="/assets/Main/video_btn.png" alt="" />
                        </Link>
                        <button className="profile-button" id ="ts">
                            <div className="profile-button img">
                                <Link to = "/login"><img src="/assets/Main/user.png" alt="User Picture" /></Link>
                            </div>
                        </button>
                    </div>
                </nav>
            </header>
        </>
    )
}
export default MainHeader;