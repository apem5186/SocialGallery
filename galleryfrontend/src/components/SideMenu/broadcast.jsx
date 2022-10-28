import MainHeader from "../Main/MainHeader"
import SideMenu from "../Main/SideMenu"
import Sidebar from "../Main/SiderBar"
import Footer from "../Main/Footer"

function Broadcast(){
    return ( 
        <>
            <MainHeader></MainHeader>
            
            <SideMenu></SideMenu>
            <Sidebar></Sidebar>
            <Footer></Footer>
        </>
    )
}
export default Broadcast