
import axios  from 'axios';
import  useEffect  from 'react';
import Footer from './Footer';
import Sidebar from './SiderBar';
import SideMenu from './SideMenu';
import MainHeader from './MainHeader';
import Content from './Content';

function Main({mainImg,setMainImg,setReply,searchTitle,pfUser,reply,setCommentArray,setPfUser,setSearchTitle}){
    // 이미지 받아오기


    return (
        <>
            {/* Header*/}
            <MainHeader
            setMainImg={setMainImg}
            ></MainHeader>

            {/* Side Menu */}
            <SideMenu></SideMenu>

            {/* Main Contents */}
            <div className='test'>

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
            </div>

            {/* Sidebar --> */}
            <Sidebar
                pfUser={pfUser}
                setPfUser={setPfUser}
                setSearchTitle={setSearchTitle}
                mainImg={mainImg}
                setMainImg={setMainImg}
                searchTitle={searchTitle}
            ></Sidebar>

            {/* Footer */}
            <Footer></Footer>
        </>
    )
}


export default Main;