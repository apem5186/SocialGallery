
import { useState } from 'react';
import  {Link} from 'react-router-dom'
import postCommentInFeed from './comment';
import axios  from 'axios';
import { useEffect } from 'react';
import Footer from './Footer';
import Sidebar from './SiderBar';
import SideMenu from './SideMenu';
import MainHeader from './MainHeader';
import Content from './Content';


function Main(){
    const [mainImg,setMainImg] = useState([])
// ProfileUser
    const [pfUser, setPfUser] = useState([])
// 검색
    const [posts, setPosts] = useState([])
    const [searchTitle, setSearchTitle] = useState("")
    let [reply, setReply] = useState([])
    let [urlPid, setUrlPid] = useState([])
    let [commentArray, setCommentArray] = useState([])


    const base_URL = "http://localhost:8080"


// 이미지 받아오기
    useEffect(()=>{
        axios.get(base_URL + '/api/post')
            .then(res =>{
                console.log(res.data.list)
                setMainImg([...res.data.list])
            })
    },[])


//    댓글불러
    useEffect(()=>{
        axios.get('http://localhost:8080/api/comment/all')
            .then(res =>{
                console.log(res.data.list)
                setReply([...res.data.list])
            })
    },[])


// 검색목록
    useEffect(()=>{
        const searchBox = async ()=>{
            const response = await axios.get("https://jsonplaceholder.typicode.com/photos?id=1&id=2&id=3&id=4")
            setPosts(response.data)
        }
        searchBox()
    },[])



    return (
        <>
            {/* Header*/}
            <MainHeader></MainHeader>



            {/* Side Menu */}
            <SideMenu></SideMenu>

            {/* Footer */}
            <Footer></Footer>
            {/* Main Contents */}
            <div className='test'>
                <h2>Search Filter</h2>

                {
                    mainImg
                        .filter( a => {
                            if(searchTitle === ""){
                                return a
                            }else if(
                                a.title.toLowerCase().includes(searchTitle.toLowerCase()))
                                return a
                        })
                        .map((a,i)=><Content
                            key={a.id}
                            mainImg={mainImg}
                            i={i}
                            pfUser={pfUser}
                            setPfUser={setPfUser}
                            reply={reply}
                            setReply={setReply}
                            commentArray={commentArray}
                            setCommentArray={setCommentArray}
                        ></Content>)
                }
            </div>

            {/* Sidebar --> */}
            <Sidebar
                pfUser={pfUser}
                setPfUser={setPfUser}
                setSearchTitle={setSearchTitle}
            ></Sidebar>


        </>
    )
}


export default Main;