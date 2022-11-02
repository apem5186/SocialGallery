
import Footer from './Footer';
import Sidebar from './SiderBar';
import SideMenu from './SideMenu';
import MainHeader from './MainHeader';
import Content from './Content';
import { useSelector,useDispatch } from "react-redux";
import { fetchMainImg } from '../../store/Store';
import { useEffect } from 'react';

function Main({rgName,setRgName}){

 let mainImg = useSelector((state)=>state.mainImg.mainList)
 let dispatch = useDispatch()    

 useEffect(()=>{
     dispatch(fetchMainImg())
 },[dispatch])


    return (
        <>
       
            {/* Header*/}
            <MainHeader></MainHeader>
            
            {/* Side Menu */}
            <SideMenu></SideMenu>
         
            {/* Main Contents */}
                {
                    mainImg
                        .map((a,i)=><Content
                            key={a.pid}
                            i={i}
                        ></Content>)
                }
            
            {/* Sidebar --> */}
            <Sidebar rgName={rgName} setRgName={setRgName}></Sidebar>

            {/* Footer */}
            <Footer></Footer>
        </>
    )
}


export default Main;