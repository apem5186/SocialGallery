import {useEffect, useState} from "react"
import axios from "axios"
import UpLoad from '../PostReg/upload';
import {Link} from "react-router-dom";
import Delete from "../PostReg/delete";
import { useSelector,useDispatch } from "react-redux";
import { fetchMainImg, fetchReply } from '../../store/commentSlice';
import Edit from "../PostReg/edit";

function Content({i}){
    const [title, setTitle] = useState('')
    const [content, setContent] = useState([])

    // base_URL
    const base_URL = "http://localhost:8080"
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

    // mainImg useSelector
    let mainImg = useSelector((state)=>state.mainImg.mainList)
    // 댓글 useSelector
    let reply = useSelector((state)=>state.reply.replyList)
    let dispatch = useDispatch()

    // MainImg, Reply dispatch
    useEffect(()=>{
        dispatch(fetchMainImg())
        dispatch(fetchReply())
    },[dispatch])

    let [users, setUsers] = useState([]);
    let [post, setPost] = useState([]);

    // 빈 댓글
    const [commentArray, setCommentArray] = useState([])

    // 댓글
    const [comment, setComments] = useState([])

    const postCommentSubmit = (e) => {
        e.preventDefault()
        setCommentArray(a=>[comment])
        setComments('')


        const headers = {
            'Content-type': 'application/json',
            'Authorization': "Bearer " + localStorage.getItem("token")
        }
        axios.post(dev_url + `/api/comment/register`,{
            users: users,
            post : post,
            comment : comment,

        } ,{headers},)
            .then(res=>{
                console.log([...res.data])
            })
    }

    const onHandleComment = e =>{
        e.preventDefault()
        setComments(e.currentTarget.value)
    }

    useEffect(()=>{
        axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
            .then(res=>{
                setUsers(res.data.data)
            })
    },[])

    useEffect(() => {
        axios.get(dev_url + "/api/post/" + mainImg[i].pid).then(
            res => {
                setPost(res.data.data)
            })
    }, [])

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
                                            <img src="/assets/Main/user.png" alt="User Picture" />
                                        </Link>
                                        <span>{mainImg[0].username}</span>
                                        {/* Upload*/}
                                        <UpLoad
                                            title={title}
                                            setTitle={setTitle}
                                            content={content}
                                            setContent={setContent}
                                        ></UpLoad>
                                        {/* Edit */}
                                        <Edit
                                            title={title}
                                            setTitle={setTitle}
                                            content={content}
                                            setContent={setContent}
                                            i={i}
                                        ></Edit>
                                        {/* Delete */}
                                        <Delete mainImg={mainImg} i={i}></Delete>
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
                                            <img src="/assets/Main/chat_btn.png" alt="" />
                                        </button>
                                        <button className="post__button">
                                            <img src="/assets/Main/love_btn.png" alt="" />
                                        </button>
                                        <button className="post__button post__button--align-right">
                                            <img src="/assets/Main/more_btn.png" alt="" />
                                        </button>
                                    </div>
                                    <div className="post__infos"   >
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
                                            <div className="comment_list">
                                                {
                                                    reply
                                                        .filter((value)=>value.pid === mainImg[i].pid)
                                                        .map((a,i)=>{
                                                            return(
                                                                <div key={a.cid}>
                                                                    <em>{a.username}</em>
                                                                    &nbsp;&nbsp;:
                                                                    <span>{a.comment}</span>
                                                                </div>)
                                                        })
                                                }

                                            </div>
                                        </div>


                                        <form onSubmit={postCommentSubmit}>
                                            <section className="post_comment_wrap" >
                                                <input
                                                    id="post_comment_input"
                                                    type="text"
                                                    placeholder="댓글 달기..."
                                                    value={comment}
                                                    onChange={onHandleComment}
                                                />
                                                <button
                                                    className="post_comment_btn"
                                                    onClick={()=>{
                                                        window.location.reload('/')
                                                    }}
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