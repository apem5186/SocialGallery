import { Link } from "react-router-dom";

function SideMenu(){

    return(
        <>
            <section className="side-menu">
                <div className="side-menu__title">
                    <div className="side-menu__title_info">
                        <a>Social Gallery</a>
                        <span></span>
                    </div>
                </div>
                {/* Side text */}
                <div className="side-menu__title-section">
                    <div className="side-menu__title-header">
                        <h2>전체 게시글</h2>
                    </div>
                    {/* Side Contents */}
                    <SideContents></SideContents>
                </div>
            </section>
        </>
    )
}

function SideContents(){
    return (
        <>
            <div className="side-menu__title-content">
                <div className="side-menu__recommend"></div>
                <div className="side-menu__recommend_info">
                    <Link to="?category=MOVIE">영화/드라마</Link>
                    <span>영화 한 편을 선정하여 결말과 특정 시퀀스 혹은 신에 대해 토론</span>
                </div>
            </div>
            <div className="side-menu__title-content">
                <div className="side-menu__recommend"></div>
                <div className="side-menu__recommend_info">
                    <Link to="?category=BROADCAST">연예/방송</Link>
                    <span>1인미디어,스위쳐,음향장비,방송케이블</span>
                </div>
            </div>
            <div className="side-menu__title-content">
                <div className="side-menu__recommend"></div>
                <div className="side-menu__recommend_info">
                    <Link to="?category=LIFE">취미/생활</Link>
                    <span>개성을 나타내는 특별한 취미를 공유</span>
                </div>
            </div>
            <div className="side-menu__title-content">
                <div className="side-menu__recommend"></div>
                <div className="side-menu__recommend_info">
                    <Link to="?category=TRAVEL">여행/음식</Link>
                    <span> 카페건물 내부/외부 모델과 케이크, 음료 등 다양한 음식이 포함된</span>
                </div>
            </div>
        </>
    )
}

export default SideMenu;