import axios from "axios";
import { useSelector } from "react-redux";


function CommentDel({a,i}){
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

    // 댓글 useSelector
    let reply = useSelector((state)=>state.reply.replyList)
    // userData useSelector
    let users = useSelector((state)=>state.userData.userDataList)


    const headers = {
        'Content-type': 'application/json',
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    const deleteComment = (cid)=>{
        axios.delete(dev_url + `/api/comment/delete/${cid}`,{headers,
            withCredentials: true,
            crossDomain: true,
            credentials: "include"})
            .then(res=>{
                window.location.reload()
            })
            .catch(()=>{
                console.log('실패',cid)
            })
    }

    return (
        <>

            {
                users.id !== a.uid
                    ? <button> </button>
                    : <button onClick={ ()=>{
                        if(window.confirm("정말 삭제합니까?")){
                            deleteComment(a.cid)
                        }else{
                            alert('취소 됐습니다.')
                        }
                    }
                    }>X</button>
            }
        </>
    )
}

export default CommentDel;