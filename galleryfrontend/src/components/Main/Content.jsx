import {useEffect, useState} from "react"
import axios from "axios"
import UpLoad from '../PostReg/upload';
import { Link } from "react-router-dom";
import postCommentInFeed from "./comment";
import {useParams} from "react-router-dom";
function Content({pfUser,setPfUser,mainImg,i}){
    let [users, setUsers] = useState([]);
    let [post, setPost] = useState([]);
    let pid = useState('')
    // 댓글
    const [comment, setComment] = useState('');
    const [commentArray, setCommentArray] = useState([]);
    const handleComment = e => {
        setComment(e.target.value);
    }
    useEffect(()=>{
        pid = post.find(1)
        axios.get(base_URL+`/api/comment/${pid}`)
            .then(res=>{
                setComment(res.data)
                console.log(res.data)
            })
    },[])
    const onCommentSubmit = e => {
        e.preventDefault();
        if (comment === '') {
            return;
        }
        setCommentArray(a => [comment, ...a]);
        setComment('');
    };
    const onCommentHandler = (e) =>{
        setComment(e.currentTarget.value)
    }

    const base_URL = "http://localhost:8080"

    // const getUsers = () => {
    //    axios.get(base_URL + "/findUserByEmail/" + localStorage.getItem("user"), {
    //
    //    }).then(res => {
    //        users = setUsers(res.data.users)
    //        return users
    //
    //    })
    // }
    //
    // const getPost = () => {
    //     axios.get(base_URL + "/api/post/" + mainImg[i].pid, {
    //
    //     }).then(res => {
    //         post = setPost(res.data.post)
    //         return post
    //     })
    // }
    useEffect(()=>{
        axios.get(base_URL + "/findUserByEmail/" + localStorage.getItem("user"))
            .then(res=>{
                setUsers(res.data.data)
            })
    },[])

    useEffect(() => {
        axios.get(base_URL + "/api/post/" + mainImg[i].pid).then(
            res => {
                setPost(res.data.data)
            })
    }, [])

        // const post = axios.get(base_URL + "/api/post/" + mainImg[i].pid)
    const commentSubmit = () => {
        const headers = {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin' : "*",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
        axios.post(base_URL + "/api/comment/" + mainImg[i].pid + "/register",{
            comment : comment,
            users : users,
            post : post
        },{
            headers
        })
            .then(result => {
                console.log('결과: ', result)
            }).catch(()=>{

            console.log(users)
        });
    }


    // id=1일 데이터 받아오기
    // const Pid = props.mainImg.filter((a,i) =>{
    //   return (a.id < 2)
    // })

    return (
        <>
            <main className="main-container">
                <section className="content-container">
                    <div className="content">
                        <div className="posts"> {/*4줄까지 */}
                            <article className="post">
                                <div className="post__header">
                                    <div className="post__profile">
                                        <Link to="#" className="post__avatar">
                                            <img src="assets/Main/user.png" alt="User Picture" />
                                        </Link>
                                        <span>
          {pfUser.name}
        </span>
                                        {/* <UpLoad></UpLoad> */}
                                        <UpLoad></UpLoad>
                                    </div>
                                </div>
                                <div className="post__content">
                                    <div className="post__medias" >
                                        <img src={`assets/Img/${mainImg[i].filePath}`} alt="" />
                                    </div>
                                </div>
                                <div className="post__footer">
                                    <div className="post__buttons">
                                        <button className="post__button">
                                            <img src="assets/Main/chat_btn.png" alt="" />
                                        </button>
                                        <button className="post__button">
                                            <img src="assets/Main/love_btn.png" alt="" />
                                        </button>
                                        <button className="post__button post__button--align-right">
                                            <img src="assets/Main/more_btn.png" alt="" />
                                        </button>
                                    </div>
                                    <div className="post__infos" onSubmit={onCommentSubmit}   >
                                        <div className="post__title">
                                            <span>{mainImg[i].title}</span>
                                        </div>
                                        <div className="post__description">
                                            <span>{mainImg[i].content}</span>
                                        </div>
                                        <div className="post__border">
                                        </div>
                                        {/*
        <!-- 댓글 --> */}
                                        <div>
                                            <ul className="comment_list">
                                                {commentArray.map((value,i) => (
                                                    <li key={i}>
                                                        <div className="user_desc">
                                                            <em>iAmUser</em>
                                                            <span>{value[i]}</span>
                                                        </div>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                        <form>
                                            <section className="post_comment_wrap" >
                                                <input
                                                    id="post_comment_input"
                                                    type="text"
                                                    placeholder="댓글 달기..."
                                                    value={comment}
                                                    onChange={handleComment}
                                                />
                                                <button
                                                    className="post_comment_btn"
                                                    onClick={onCommentSubmit}
                                                >
                                                    <i className='bx bx-send' ></i>
                                                </button>
                                            </section>
                                        </form>
                                        <span className="post__date-time">more</span>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </div>
                </section>

            </main>
        </>

    )
}

export default Content