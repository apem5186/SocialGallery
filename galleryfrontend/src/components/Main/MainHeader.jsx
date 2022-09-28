import { Link } from "react-router-dom";
import axios from "axios";

function MainHeader({setMainImg}){
    const base_URL = "http://localhost:8080"

    const getAll = (e) => {
        axios.get(base_URL + '/api/post')
            .then(res => {
                setMainImg([...res.data.list])
            }).catch(
                console.log("왜안돼 ㅅ발")
        )
    }

    return(
        <>
            <header className="header">
                <nav className="header__content">
                    <div className="header__buttons">
                        <Link to='/'><p onClick={getAll}>Social Gallery</p></Link>
                    </div>

                    <div className="header__buttons header__buttons--desktop">
                        <Link to="#">
                            <img src="/assets/Main/home_btn.jpg" alt="" />
                        </Link>
                        <Link to="#">
                            <img src="/assets/Main/msg_btn.png" alt="" />
                        </Link>
                        <Link to="#">
                            <img src="/assets/Main/video_btn.png" alt="" />
                        </Link>
                        <button className="profile-button">
                            <div className="profile-button img">
                                <Link to = "/login"><img src="/assets/main/user.png" alt="User Picture" /></Link>
                            </div>
                        </button>
                    </div>
                </nav>
            </header>
        </>
    )
}
export default MainHeader;