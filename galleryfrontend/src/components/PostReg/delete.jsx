import  axios  from 'axios';

function Delete({mainImg,i}){
    const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"
    const headers = {
        'Content-type': 'application/json',
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    function onHandelDelete(pid){
        axios.delete(dev_url+`/api/post/delete/${pid}`, {headers})
            .then(res=>{
                window.location.reload()
            })
    }



    return (
        <>
            <div>
            <span className="material-icons outlined" onClick={()=>{onHandelDelete(mainImg[i].pid)}}>
                delete
            </span>
            </div>
        </>
    )
}
export default Delete