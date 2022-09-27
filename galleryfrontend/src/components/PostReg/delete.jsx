import  axios  from 'axios';
import { useState } from 'react';

function Delete({mainImg,i}){
    const headers = {
        'Content-type': 'application/json',
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    function onHandelDelete(pid){
        axios.delete(`http://localhost:8080/api/post/delete/${pid}`, {headers})
            .then(res=>{
                window.location.reload()
            })
    }



    return (
        <>
            <div>
            <span class="material-icons outlined" onClick={()=>{onHandelDelete(mainImg[i].pid)}}>
                delete
            </span>
            </div>
        </>
    )
}
export default Delete