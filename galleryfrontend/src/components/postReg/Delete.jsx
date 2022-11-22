import  axios  from 'axios';
import { useSelector } from 'react-redux';
import { useDispatch } from "react-redux";
import { setIsLogin } from "../../store/Store";
import { useEffect } from 'react';

function Delete({i}){

    let postAll = useSelector((state)=>state.postAll.postAllList)
    const isLogin = useSelector((state)=>state.isLogin.isLoginList)
    const dispatch = useDispatch()

    const base_URL = 'http://localhost:8080'
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


    const headers = {
        'Content-type': 'application/json',
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    useEffect(()=>{
        axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
            .then((res)=>{
                dispatch(setIsLogin(res.data.data.isLogin))
            })
    },[])
    function onHandelDelete(pid){
        axios.delete(dev_url + `/api/post/delete/${pid}`, {headers})
            .then(res=>{
                window.location.reload()
            })
    }



    return (
        <>
            <div>
            <span className="material-icons outlined" onClick={()=>{
                if (isLogin === false) {
                    alert('로그인 후 사용해주세요.')

                } else if (window.confirm("정말 삭제합니까?")) {
                    onHandelDelete(postAll[i].pid)

                } else{
                    alert('취소 됐습니다.')
                }
            }}>
                delete
            </span>
            </div>
        </>
    )
}
export default Delete