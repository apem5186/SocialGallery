import { useState } from "react"
import axios from "axios"

function UpLoadForm() {
	const [images, setImages] = useState([])
	const [title, setTitle] = useState(null)
	const [comments, setComments] = useState([])

		// Img 미리보기
		const [ imgs, setImgs ] = useState('')
		const [ previewImg, setPreviewImg ] = useState('')

		// 미리보기
		const insertImg = (e) => {
		let reader = new FileReader()

		if(e.target.files[0]) {
		reader.readAsDataURL(e.target.files[0])
		setImgs(e.target.files[0])
		console.log(e.target.files[0])
		}

		reader.onloadend = () => {
		const previewImgUrl = reader.result

		if(previewImgUrl) {
		setPreviewImg([...previewImg, previewImgUrl])
		}
	}
}

		// Img Data Server 전송

		const handleImgFile = (e) => {
		let file = e.target.files[0]
		setImages(file)
		}

		const handleImgUpload = async (e) => {
		let formdata = new FormData()

		formdata.append('image', e.target.files)
		formdata.append('name', 'HJ-C')

		axios({
				method : "POST",
				url : '',
				headers : {
				'Content-Type': 'multipart/form-data',
				},
				data : formdata,
				})
				.then(res => {
				console.log(res.data)
				})
			}
		// 제목,글 Data Server 전송
		const onHandlePostTitle = (e) =>{
			setTitle(e.currentTarget.value)
		}
		const onHandlePostComments = (e) =>{
			setComments(e.currentTarget.value)
		}
		const postSubmit = () => {
			axios.post('',{
				title : title,
				comments : comments,
			},{
				headers: {
					'Content-Type': 'application/json'
					}
			})
			.then(res => res.json())
			.then(result => console.log('결과: ', result));
		}

return (
<>
	<div className="popup_layer" id="popup_layer">
		<div className="popup_box">

			<div className="popup_cont">
				<div className="popup_header">
					<div className="back"></div>
					<div className="title">새 게시물 만들기</div>
					{/* 버튼 */}
					<button className="share"type="submit" onClick= { (e)=>{
						handleImgUpload(e)
						postSubmit(e)
					}}>
						공유하기
					</button>
					<span className="material-icons" onClick={closePop}>
							close
					</span>

				</div>
				<div className="popup_contents">
					<div className="popup_photoarea">
						<div className="filebox">
							<input className="upload-name" placeholder="첨부파일" />
							<label htmlFor="file">파일찾기</label>
							{/* 파일찾기 */}
							<input type="file" id="file" name="files"onChange={(e)=>{
								 insertImg(e)
								 handleImgFile(e)
							}} />
							<div>
								<img src={previewImg} alt="" />
							</div>
							<div>
								{imgs.name}
							</div>
						</div>
					</div>

					<div className="popup_photoinfo">
						<div className="photoinfo_name">
							<img src="assets/Main/user.png" alt="User Picture" />
							<span>user1</span>
						</div>

						<div className="photoinfo_contents">
							<div className="title">
								<span className="material-icons">drive_file_rename_outline</span>
								<span>Title</span>
							</div>
							<input 
								type="text" 
								id="cont_title" 
								placeholder="제목을 입력해주세요." 
								onChange={onHandlePostTitle}
								/>

							<div className="contents">
								<span className="material-icons">list_alt</span>
								<span>Contents</span>
							</div>
							<input 
								type="text" 
								id="cont_story" 
								placeholder="내용을 입력해주세요." 
								onChange={onHandlePostComments}
								/>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>



</>
)
}

function closePop() {
	document.getElementById("popup_layer").style.display = "none";
}
export default UpLoadForm;