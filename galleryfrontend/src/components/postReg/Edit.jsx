import EditForm from './EditForm';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { setIsLogin } from '../../store/Store';
import { useDispatch } from 'react-redux';

function Edit({imgs,setImgs,previewImg,setPreviewImg,i}){

    const isLogin = useSelector((state)=>state.isLogin.isLoginList)
    const dispatch = useDispatch()

    const base_URL = 'http://localhost:8080'
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"

    return (
        <>
            {/*Open 팝업*/}
            <div onClick={()=>{
                axios.get(dev_url + "/findUserByEmail/" + localStorage.getItem("user"))
                    .then((res)=>{
                        dispatch(setIsLogin(res.data.data.isLogin))
                    })
                isLogin === false
                    ? alert('로그인 후 사용해주세요.')
                    : openPop()
            }}>
                <div className="popup_start">
                        <span className="material-icons outlined">
                            edit
                        </span>
                </div>
            </div>
            {/* 수정 컨텐츠 영역*/}
            <EditForm
                imgs={imgs}
                setImgs={setImgs}
                previewImg={previewImg}
                setPreviewImg={setPreviewImg}
                i={i}
            ></EditForm>

        </>
    )
}

function openPop() {
    document.getElementById("popup_layer1").style.display = "block";
}


export default Edit