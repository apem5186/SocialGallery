import EditForm from './editForm';

function Edit({title,setTitle,content,setContent,i}){

    return (
        <>
            {/*Open 팝업*/}
            <div onClick={openPop}>
                <div className="popup_start">
                        <span className="material-icons outlined">
                            edit
                        </span>
                </div>
            </div>
            {/* 수정 컨텐츠 영역*/}
            <EditForm
                title={title}
                setTitle={setTitle}
                content={content}
                setContent={setContent}
                i={i}
            ></EditForm>

        </>
    )
}

function openPop() {
    document.getElementById("popup_layer1").style.display = "block";
}


export default Edit