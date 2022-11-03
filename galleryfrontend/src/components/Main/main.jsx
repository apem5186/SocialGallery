
import Footer from './Footer';
import Sidebar from './SiderBar';
import SideMenu from './SideMenu';
import MainHeader from './MainHeader';
import Content from './Content';
import { useSelector,useDispatch } from "react-redux";
import { useEffect } from 'react';
import axios from "axios";
import {setMainImg} from "../../store/Store";

function Main({rgName,setRgName}){

 let mainImg = useSelector((state)=>state.mainImg.mainList)
 let dispatch = useDispatch()
 const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


    useEffect(()=>{
        axios.get(dev_url + '/api/post')
            .then((res) => {
                dispatch(setMainImg([...res.data.list]));
            })
            .catch((err) => {
                console.log(err);
            })
    },[])


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