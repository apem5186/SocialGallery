import UpLoadForm from './UpLoadForm';

function Upload({imgs,setImgs,previewImg,setPreviewImg}){

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


export default Upload