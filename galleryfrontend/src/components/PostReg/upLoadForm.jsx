import axios from "axios"
import { useSelector, useDispatch } from 'react-redux';
import { setPostContent, setPostTitle } from "../../store/commentSlice";


function UpLoadForm({imgs,setImgs,previewImg,setPreviewImg}) {

    const title = useSelector((state)=>state.postTitle.postTitleList)
    const content = useSelector((state)=>state.postContent.postContentList)
    const dispatch = useDispatch()
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


// 미리보기
    const insertImg = (e) => {
        let reader = new FileReader()

        if(e.target.files[0]) {
            reader.readAsDataURL(e.target.files[0])
            setImgs(e.target.files[0])
        }

        reader.onloadend = () => {
            let previewImgUrl = reader.result

            if(previewImgUrl) {
                setPreviewImg([...previewImg, previewImgUrl])
            }
        }
        
    }

// 제목,글 Data Server 전송
    const onHandlePostTitle = (e) =>{
        dispatch(setPostTitle(e.currentTarget.value))
    }
    const onHandlePostComments = (e) =>{
        dispatch(setPostContent(e.currentTarget.value))
    }

    const postSubmit = (e) => {
        
        e.preventDefault();
        const headers = {
            'Content-type': 'multipart/form-data',
            'Authorization': "Bearer " + localStorage.getItem("token")
        }
        const formData = new FormData()
        formData.append('files', imgs)
        formData.append('usersId', localStorage.getItem('uid'))
        formData.append('title', title)
        formData.append('content', content)

        axios.defaults.headers.post = null
        axios.post(dev_url + '/api/post/upload',formData, {headers})
            .then(()=>{
                window.location.reload()
            })
    }

    return (
        <>
            <div className="popup_layer" id="popup_layer">
                <div className="popup_box">
                    <form onSubmit={postSubmit}>
                        <div className="popup_cont">
                            <div className="popup_header">
                                <div className="back"></div>
                                <div className="title">새 게시물 만들기</div>
                                {/* 버튼 */}
                                <button className="share" type="submit" >
                                    공유하기
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
                                        <input type="file" id="file" name="files" onChange={(e)=>{
                                            insertImg(e)
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

                                    <div className="photoinfo_contents">
                                        <div className="title">
                                            <span className="material-icons">drive_file_rename_outline</span>
                                            <span>Title</span>
                                        </div>
                                        <input type="text" id="cont_title" placeholder="제목을 입력해주세요." onChange={onHandlePostTitle} />

                                        <div className="contents">
                                            <span className="material-icons">list_alt</span>

                                            <span>Contents</span>
                                        </div>
                                        <input type="text" id="cont_story" placeholder="내용을 입력해주세요." onChange={onHandlePostComments} />
                                    
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

            </div>



        </>
    )
}

function closePop() {
    document.getElementById("popup_layer").style.display = "none";
}
export default UpLoadForm;