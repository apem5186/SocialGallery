import axios from 'axios';
import { useSelector, useDispatch } from 'react-redux';
import { setPostContent, setPostTitle } from "../../store/commentSlice";
import {useState} from "react";

function EditForm({imgs,setImgs,previewImg,setPreviewImg,i}){

    let mainImg = useSelector((state)=>state.mainImg.mainList) 
    const title = useSelector((state)=>state.postTitle.postTitleList)
    const content = useSelector((state)=>state.postContent.postContentList)
    const dispatch = useDispatch()
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"
    const [category, setCategory] = useState('')

    // 미리보기
    const editImg = (e) => {
        const reader = new FileReader()

            reader.readAsDataURL(e.target.files[0])
            setImgs(e.target.files[0])

        reader.onloadend = () => {
            let previewImgUrl = reader.result

            if(previewImgUrl) {
                setPreviewImg([...previewImg, previewImgUrl])
            }
        }
    }

    

      // 제목,글 Data Server 전송
    const onHandleUpdateTitle = (e) =>{
        dispatch(setPostTitle(e.currentTarget.value))
    }
    const onHandleUpdateComments = (e) =>{
        dispatch(setPostContent(e.currentTarget.value))
    }
    const onHandleCategory = (e)=>{
        setCategory(e.target.value)
    }

    //Put
    const putSubmit = (pid) => {
        
        const headers = {
            'Content-type': 'multipart/form-data',
            'Authorization': "Bearer " + localStorage.getItem("token")
        }
        const formData = new FormData()
        formData.append('files', imgs)
        formData.append('usersId', localStorage.getItem('uid'))
        formData.append('title', title)
        formData.append('content', content)
        formData.append('category', category)

        axios.defaults.headers.post = null
        axios.put(dev_url + `/api/post/modify/${pid}`,formData, {headers})
            .then(()=>{
                
            })
    }

    return(
        <div className="popup_layer" id="popup_layer1">
        <div className="popup_box">
            <form>
                <div className="popup_cont">
                    <div className="popup_header">
                        <div className="back"></div>
                        <div className="title">게시물 수정하기</div>
                        {/* 버튼 */}
                        <button 
                            className="share" 
                            type="submit"
                            onClick={()=>{putSubmit(mainImg[i].pid)}}
                            >
                            수정하기
                        </button>
                        <span className="material-icons" onClick={closePop}>
                    close
                </span>

                    </div>
                    <div className="popup_contents">
                        <div className="popup_photoarea">
                            <div className="filebox">
                                <label htmlFor="file">+</label>
                                {/* 파일찾기 */}
                                <input type="file" id="files" name="file" onChange={(e)=>{
                                    editImg(e)
                                }} />
                                        <div>
                                            <img className="preImg" src={previewImg} alt="" />
                                        </div>
                                        <div className="preText">{imgs.name}</div>
                            </div>
                        </div>

                        <div className="popup_photoinfo">
                            <div className="photoinfo_name">
                                <img src="/assets/Main/user.png" alt="User Picture" />
                                <span>user1</span>
                            </div>
                            <div>
                                <form onChange={onHandleCategory}>
                                <input type='radio' name='category' value='broadcast'/>영화/드라마<br></br>
                                <input type='radio' name='category' value='life' />연예/방송<br></br>
                                <input type='radio' name='category' value='movie'/>취미/생활<br></br>
                                <input type='radio' name='category' value='travel' />여행/음식<br></br>
                                </form>
                            </div>
                            <div className="photoinfo_contents">
                                <div className="title">
                                    <span className="material-icons">drive_file_rename_outline</span>
                                    <span>Title</span>
                                </div>
                                <input type="text" 
                                    id="cont_title" 
                                    placeholder="제목을 수정해주세요." 
                                    onChange={onHandleUpdateTitle}
                                    />

                                <div className="contents">
                                    <span className="material-icons">list_alt</span>
                                    <span>Contents</span>
                                </div>
                                <input type="text" 
                                    id="cont_story"
                                    placeholder="내용을 수정해주세요." 
                                    onChange={onHandleUpdateComments}
                                    />
                            
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

    </div>
    )
}
function closePop() {
    document.getElementById("popup_layer1").style.display = "none";
}
export default EditForm;