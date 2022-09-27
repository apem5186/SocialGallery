import './App.css';
import './components/PostReg/upload.css'
import Login from './components/Login/login';
import Main from './components/Main/main';
import './components/Login/login.css'
import './components/Main/main.css'
import { Routes, Route } from 'react-router-dom'
import UpLoad from './components/PostReg/upload';
import Movie from "./components/Sidemenu/movie";
import Broadcast from "./components/Sidemenu/broadcast";
import Life from "./components/Sidemenu/life";
import Travel from "./components/Sidemenu/travel";
import { useState } from 'react';
import { useEffect } from 'react';
import axios from 'axios';


function App() {
    const [mainImg,setMainImg] = useState([])
    const [searchTitle, setSearchTitle] = useState("")
    let [reply, setReply] = useState([])
    let [commentArray, setCommentArray] = useState([])
    const [pfUser, setPfUser] = useState([])

    const base_URL = "http://localhost:8080"

    useEffect(()=>{
        axios.get(base_URL + '/api/post')
            .then(res =>{
                setMainImg([...res.data.list])
            })
    },[])


    //    댓글불러
    useEffect(()=>{
        axios.get('http://localhost:8080/api/comment/all')
            .then(res =>{
                setReply([...res.data.list])
            })
    },[])




    return (
        <div className="App">
            <Routes>
                {/* Main */}
                <Route exact path="/" element={
                    <Main
                        mainImg={mainImg}
                        setMainImg={setMainImg}
                        searchTitle={searchTitle}
                        setSearchTitle={setSearchTitle}
                        reply={reply}
                        setReply={setReply}
                        commentArray={commentArray}
                        setCommentArray={setCommentArray}
                        pfUser={pfUser}
                        setPfUser={setPfUser}

                    ></Main>
                }></Route>
                <Route path="/:pid" element={
                    <Main
                        mainImg={mainImg}
                        setMainImg={setMainImg}
                        searchTitle={searchTitle}
                        setSearchTitle={setSearchTitle}
                        reply={reply}
                        setReply={setReply}
                        commentArray={commentArray}
                        setCommentArray={setCommentArray}
                        pfUser={pfUser}
                        setPfUser={setPfUser}
                    ></Main>
                }></Route>

                <Route path="/upload" element={<UpLoad></UpLoad>}></Route>

                {/* Login */}
                <Route path="/login/" element={<Login></Login>}></Route>
                <Route path="/login/:location" element={<Login></Login>}></Route>

                {/* SideMenu */}
                <Route path="/movie" element={<Movie></Movie>}></Route>
                <Route path="/broadcast" element={<Broadcast
                    mainImg={mainImg}
                    pfUser={pfUser}
                    setPfUser={setPfUser}
                    setCommentArray={setCommentArray}
                    reply={reply}
                ></Broadcast>}></Route>
                <Route path="/life" element={<Life></Life>}></Route>
                <Route path="/travel" element={<Travel></Travel>}></Route>
            </Routes>
        </div>
    );
}

export default App;