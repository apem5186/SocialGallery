import MainHeader from "../Main/MainHeader"
import SideMenu from "../Main/SideMenu"
import Sidebar from "../Main/SiderBar"
import Footer from "../Main/Footer"
import Content from "../Main/Content"

function Broadcast({mainImg,pfUser,reply,setCommentArray}){
    return (
        <>
            <MainHeader></MainHeader>

            {/* Main Contents */}

            {
                mainImg
                    .map((a,i)=><Content
                        key={a.pid}
                        mainImg={mainImg}
                        i={i}
                        pfUser={pfUser}
                        reply={reply}
                        setCommentArray={setCommentArray}
                    ></Content>)
            }

            <SideMenu></SideMenu>
            <Sidebar></Sidebar>
            <Footer></Footer>
        </>
    )
}
export default Broadcast