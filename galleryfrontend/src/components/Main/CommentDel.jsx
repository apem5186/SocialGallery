import axios from "axios";
import { useSelector } from 'react-redux';

function CommentDel({i}){

    const reply = useSelector((state)=> state.reply.replyList)
    const mainImg = useSelector((state)=>state.mainImg.mainList)

    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


    const headers = {
        'Content-type': 'application/json',
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    const deleteComment = (cid)=>{
        axios.delete(dev_url + `/api/comment/${mainImg[i].pid}/delete/${cid}`,{headers,
            withCredentials: true,
            crossDomain: true,
            credentials: "include"})
            .then(res=>{
                window.location.reload()
                console.log('성공')
            })
    }

    return (
        <>
            <button onClick={()=>{deleteComment(reply[i].cid)}}>X</button>
        </>
    )
}

export default CommentDel;