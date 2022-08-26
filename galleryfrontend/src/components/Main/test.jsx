import React from 'react'
import axios from 'axios'
import { useEffect,useState } from 'react'

function Test() {
    const [ users, setUsers] = useState([])

    useEffect(()=>{
        axios.get(`https://jsonplaceholder.typicode.com/users?id=1`)
            .then(res=>{
                setUsers(res.data)
            })
    },[])

  return (
    <>
    <div>
    <div>ㅋ</div>
        {
            users.map((a,i)=>{
                return(
                    <>
                    <div key={a.id}>{a.name}</div>
                    {/* <div>{Object.values(a.email)}</div> */}
                    </>
                )
            })
        }
    <div>ㅋ</div>
    </div>
    </>
  )
}

export default Test

