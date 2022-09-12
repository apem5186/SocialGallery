import {useEffect, useState} from "react"
import axios from "axios"
import UpLoad from '../PostReg/upload';
import {Link} from "react-router-dom";

function Content({pfUser,setPfUser,mainImg,i,setReply,reply}){
    let [users, setUsers] = useState([]);
    let [post, setPost] = useState([]);
    let [pid, setPid] = useState('')
    //let [uid, setUid] = useState('')
    const uid = localStorage.getItem('uid')
    // 댓글
    const [mainComment, setMainComment] = useState([])
    const [comment, setComments] = useState([])
    let [commentArray, setCommentArray] = useState([])

    const base_URL = "http://localhost:8080"


    const postCommentSubmit = (e) => {
        e.preventDefault()
        setPid(mainImg[i].pid)

        setCommentArray(a=>[comment,...a])
        setCommentArray(a=>[reply,...a])
        setComments('')

        const headers = {
            'Content-type': 'application/json',
            'Authorization': "Bearer " + localStorage.getItem("token")
        }
        axios.post(`http://localhost:8080/api/comment/${pid}/register`,{
            users: users,
            post : post,
            comment : comment,

        } ,{headers},)
            .then(res=>{
                console.log([...res.data])
            })
    }

    const onHandleComment = e =>{
        setComments(e.currentTarget.value)
    }

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
                                <div className="post__id">
                                    <div className="post__pid">
                                        <span>{mainImg[i].pid}</span>
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
                                            <ul className="comment_list">

                                                {
                                                    commentArray.map((a)=>{
                                                        return (
                                                            <>
                                                            {/*<em>{a.username} &nbsp;</em>*/}
                                                            {/*<span>{a.comment}</span>*/}
                                                                <span>{a}</span>
                                                                <br></br>
                                                            </>
                                                        )
                                                    })
                                                }
                                            </ul>
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
                                                    // onClick={onCommentSubmit}
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