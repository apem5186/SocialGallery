import {Link} from 'react-router-dom'
import UpLoadForm from './upLoadForm';

function UpLoad({title,setTitle,content,setContent}){

    return (
        <>
            {/*Open 팝업*/}
            <div onClick={openPop}>
                <button className="popup_start">
                        <span className="material-icons uploadtext">
                            add_circle_outline
                        </span>
                </button>
            </div>
            {/* 팝업 컨텐츠 영역*/}
            <UpLoadForm
                title={title}
                setTitle={setTitle}
                content={content}
                setContent={setContent}
            ></UpLoadForm>

        </>
    )
}

function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}


export default UpLoad