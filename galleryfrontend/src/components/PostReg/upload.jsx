import {Link} from 'react-router-dom'

function UpLoad(){
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
			<div className="popup_layer" id="popup_layer">
				<div className="popup_box">
		
					{/* <!--팝업 컨텐츠 영역--> */}
					<div className="popup_cont">
						<div className="popup_header">
							<div className="back">
							</div>
							<div className="title">새 게시물 만들기</div>
							<div className="share">공유하기</div>
						</div>
						<div className="popup_contents">

							<div className="popup_photoarea">
								<div className="filebox">
									
									<input className="upload-name" defaultValue="첨부파일" placeholder="첨부파일"/>
									<label htmlFor="file">파일찾기</label>
									<input type="file" id="file"/>
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
									<input type="text" id="cont_title" placeholder="제목을 입력해주세요." />
		
									<div className="contents">
										<span className="material-icons">list_alt</span>
										<span>Contents</span>
									</div>
									<textarea id="cont_story" name="story" rows="5" cols="33" placeholder="내용을 입력해주세요."></textarea>
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