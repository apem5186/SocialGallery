import {Link} from 'react-router-dom'
import UpLoadForm from './upLoadForm';

function UpLoad(props){

return (
<>
	{/*Open 팝업*/}
	<Link to="#" onClick={openPop}>
	<div>
		<button className="popup_start">
			<span className="material-icons uploadtext">
				add_circle_outline
			</span>
		</button>
	</div>
	</Link>
	{/* 팝업 컨텐츠 영역*/}
	<UpLoadForm></UpLoadForm>

</>
)
}

function openPop() {
document.getElementById("popup_layer").style.display = "block";
}


export default UpLoad