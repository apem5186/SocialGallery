
import Footer from './Footer';
import Sidebar from './SiderBar';
import SideMenu from './SideMenu';
import MainHeader from './MainHeader';
import Content from './Content';
import { useSelector} from "react-redux";
import {useDispatch} from "react-redux";
import {useEffect, useState} from 'react';
import {setPostAll} from "../../store/Store";
import axios from 'axios';

function Main({rgName,setRgName}){

    let postAll = useSelector((state)=>state.postAll.postAllList)
    let dispatch = useDispatch()
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


    useEffect(()=>{
        axios.get(dev_url+'/api/post')
            .then((res) => {
                //TODO :: 뭐가 에런지 모름
                dispatch(setPostAll([...res.data.list]));
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
                postAll
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