import EditForm from './EditForm';

function Edit({imgs,setImgs,previewImg,setPreviewImg,i}){

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