import {Link} from 'react-router-dom'
import { useState } from 'react';
import { axios } from 'axios';
import { useEffect } from 'react';

function UpLoad(){
	const [ imgs, setImgs ] = useState('')
	const [ previewImg, setPreviewImg ] = useState('')
	const [ title, setTitle] = useState('')
	const [ content, setContent] = useState('')

	const onTitleHandler = (e) => {
		setTitle(e.currentTarget.value)
	}
	const onContentHandler = (e) => {
		setContent(e.currentTarget.value)
	}
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

	// Img Data Server Post
	const handleClick = async (e) =>{
		e.preventDefault();
		const postImg = e.target.files[0]
		const formdata = new FormData()

		let dataSet ={
			title : title,
			content : content,
		}

		formdata.append('data', postImg);
		formdata.append('data', JSON.stringify(dataSet) )

		await axios({
			method:'post',
			url : '',
			data : formdata,
			headers : {
				'Content-Type': 'multipart/form-data',
			},
		})
		.then(res => console.log(res));
	}

	// Title Data Server 전송

	return (
		<>
			{/*
			<!-- 팝업 임시 테스트  시작 --> */}
			<Link to ="#" onClick={openPop}>
				<div>
					<button className="popup_start">
						<span className="material-icons uploadtext">
							add_circle_outline
						</span>
					</button>
				</div>
			</Link>
			<form onSubmit={(e)=> handleClick(e)}>
			<div className="popup_layer" id="popup_layer">

				{/* form */}
				<div className="popup_box">
		
					{/* <!--팝업 컨텐츠 영역--> */}
					<div className="popup_cont">
						<div className="popup_header">
							<div className="back">
							</div>
							<div className="title">새 게시물 만들기</div>
							<button 
								className="share" 
								type="submit">
								공유하기</button>
						</div>
						<div className="popup_contents">

							<div className="popup_photoarea">
								<div className="filebox">

									<input className="upload-name" defaultValue="첨부파일" placeholder="첨부파일"/>
									<label htmlFor="file">파일찾기</label>
									<input 
										type="file"
										id="file"
										name="files"
										onChange={(e) => insertImg(e)}
										/>
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
										multiple="multiple"
										onChange={onTitleHandler}
										/>
		
									<div className="contents">
										<span className="material-icons">list_alt</span>
										<span>Contents</span>
									</div>
									<input 
										type="text" 
										id="cont_story" 
										placeholder="내용을 입력해주세요."
										multiple="multiple"
										onChange={onContentHandler}
										 />
								</div>
							</div>
						</div>
					</div>
				</div>

				{/*
				<!--팝업 버튼 영역--> */}
				<div className="popup_btn">
				<Link to ="#" onClick={closePop}>
						<span className="material-icons">
							close
						</span>
					</Link>
				</div>
				
			</div>		
			</form>
			</>
	)
}
//팝업 띄우기
function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}

//팝업 닫기
function closePop() {
    document.getElementById("popup_layer").style.display = "none";
}

export default UpLoad