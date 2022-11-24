import {useEffect, useState} from "react"
import axios from "axios"
import UpLoad from '../postReg/Upload';
import {Link, useNavigate} from "react-router-dom";
import Delete from "../postReg/Delete";
import { useSelector,useDispatch } from "react-redux";
import { setPostAll, setReply, setUserData } from '../../store/Store';
import Edit from "../postReg/Edit";
import CommentDel from "./CommentDel";
import { fetchIsLogin } from '../../store/Store';


function Content({i}){
    // Img 미리보기
    const [ imgs, setImgs ] = useState('')
    const [ previewImg, setPreviewImg ] = useState('')
    // 빈 댓글
    const [commentArray, setCommentArray] = useState([])
    const navigate = useNavigate()


    // base_URL
    const base_URL = "http://localhost:8080"
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

    // mainImg useSelector
    let postAll= useSelector((state)=>state.postAll.postAllList)
    // 댓글 useSelector
    let reply = useSelector((state)=>state.reply.replyList)
    // isLogin useSelector
    let isLogin = useSelector((state)=>state.isLogin.isLoginList)
    // userData useSelector
    let users = useSelector((state)=>state.userData.userDataList)

    let dispatch = useDispatch()


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
            pid : postAll[i].pid,
            comment : comment,

        } ,{headers},)
            .then(res=>{
            })
    }

    const onHandleComment = e =>{
        e.preventDefault()
        setComments(e.currentTarget.value)
    }

    // User Data
    useEffect(()=>{
        axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
            .then(res=>{
                dispatch(setUserData(res.data.data))
                // if (res.data.data.isLogin === false) {
                //     if (localStorage.getItem("token").length > 0) {
                //         alert("토큰이 만료되었습니다. 다시 로그인 해주세요.")
                //         console.log(res.data.data.isLogin)
                //         localStorage.clear()
                //         console.log("localstorage cleared")
                //         navigate("/login")
                //     }
                // }
            })
    },[])

    const userDataAll = async ()=>{
        await axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
            .then(res=>{
                dispatch(setUserData(res.data.data))
            })
    }



    // 카테고리별 페이지
    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        let category = params.get("category")

        if (category === null) {
            axios.get(dev_url+ `/api/post`).then(
                res => {
                })
        } else if (category) {
            axios.get(dev_url + `/api/post/category?category=` + category)
                .then(res => {
                        dispatch(setPostAll([...res.data.list]))
                    }
                )
        }
    }, [])


    // 댓글
    useEffect(()=>{
        axios.get(dev_url + '/api/comment/all')
            .then((res) => {
                dispatch(setReply([...res.data.list]));
            })
    },[])



    //isLogin
    useEffect(()=>{
        fetchIsLogin()
    },[dispatch])



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
                                        <span>{postAll[i].username}</span>
                                        {/* Upload*/}
                                        <UpLoad
                                            imgs={imgs}
                                            setImgs={setImgs}
                                            previewImg={previewImg}
                                            setPreviewImg={setPreviewImg}
                                        ></UpLoad>
                                        {/* Edit */}
                                        <Edit
                                            i={i}
                                            imgs={imgs}
                                            setImgs={setImgs}
                                            previewImg={previewImg}
                                            setPreviewImg={setPreviewImg}
                                        ></Edit>
                                        {/* Delete */}
                                        <Delete i={i}></Delete>
                                    </div>
                                </div>
                                <div className="post__content">
                                    <div className="post__medias" >
                                        <img src={`assets/Img/${postAll[i].filePath}`} alt="" />
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
                                            <span>{postAll[i].title}</span>
                                        </div>
                                        <div className="post__description">
                                            <span>{postAll[i].content}</span>
                                        </div>
                                        <div className="post__border">
                                        </div>
                                        {/*
        <!-- 댓글 --> */}
                                        <div>
                                            <div className="comment_list">
                                                {
                                                    reply
                                                        .filter((value)=>value.pid === postAll[i].pid)
                                                        .map((a,i)=>{
                                                            return(
                                                                <div key={a.cid}>
                                                                    <em>{a.username}</em>
                                                                    &nbsp;&nbsp;:
                                                                    <span>{a.comment}</span>

                                                                    <CommentDel a={a} i={i}></CommentDel>
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
                                                    onClick={userDataAll}
                                                />
                                                <button
                                                    className="post_comment_btn"
                                                    onClick={()=>{
                                                        isLogin === false
                                                            ? alert('로그인을 해주세요.')
                                                            : window.location.reload('/')
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