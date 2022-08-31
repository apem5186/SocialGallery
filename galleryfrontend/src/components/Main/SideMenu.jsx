import { Link } from "react-router-dom";

function SideMenu(){
    return(
        <>
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
        </>
    )
}

function SideContents(){
    return (
        <>
            <div className="side-menu__title-content">
                <div className="side-menu__recommend"></div>
                <div className="side-menu__recommend_info">
                    <Link to="#">카페에 다녀오고..</Link>
                    <span>동해물과 백두산이 마르고</span>
                </div>
            </div>
        </>
    )
}

export default SideMenu;