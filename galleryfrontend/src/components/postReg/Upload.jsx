import UpLoadForm from './UpLoadForm';
import { useSelector } from 'react-redux';
import axios from 'axios';
import { setIsLogin } from '../../store/Store';
import { useDispatch } from 'react-redux';

function UpLoad({imgs,setImgs,previewImg,setPreviewImg}){

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
                <button className="popup_start">
                        <span className="material-icons uploadtext">
                            add_circle_outline
                        </span>
                </button>
            </div>
            {/* 팝업 컨텐츠 영역*/}
            <UpLoadForm
                imgs={imgs}
                setImgs={setImgs}
                previewImg={previewImg}
                setPreviewImg={setPreviewImg}
            ></UpLoadForm>

        </>
    )
}

function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}


export default UpLoad