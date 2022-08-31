import { useState } from "react";
import Toggle from "./toggle";
import  {Link} from 'react-router-dom'
import axios from "axios";
import {useNavigate} from 'react-router-dom'

function Login() {
  const navigate = useNavigate()

  let accessToken = null;

  // 로그인 Form
  const [email,setEmail] = useState('');
  const [pw,setPw] = useState('');
  let usersId = useState('');

  const onEmailHandler = e =>{
    setEmail(e.currentTarget.value)
  }
  const onPwHandler = e =>{
    setPw(e.currentTarget.value)
  }
  const onSubmit = e => {
    e.preventDefault();
  }

  // 회원가입 Form
  const [rgEmail, setRgEmail] = useState(''); // rg = Register
  const [rgPw, setRgPw] = useState('');
  const [rgName, setRgName] = useState('');
  const [rgTel, setRgTel] = useState('');

  const onRgEmailHandler = (e) => {
      setRgEmail(e.currentTarget.value)
  }

  const onRgPwHandler = (e) => {
      setRgPw(e.currentTarget.value)
  }

  const onRgNameHandler = (e) => {
    setRgName(e.currentTarget.value)
  }
  const onRgTelHandler = (e) => {
    setRgTel(e.currentTarget.value)
  }

  const base_url = "http://localhost:8080"

  // 로그인 fetch
  // const signIn = (e) => {
  //   e.preventDefault()
  //   fetch(base_url + '/v1/login', {
  //     method: 'POST',
  //     headers: {
  //       "Content-Type": "application/json"
  //     },
  //     body: JSON.stringify({
  //       email: email,
  //       password: pw,
  //     }),
  //   })
  //     .then(res => res.json())
  //     .then(result => console.log('결과', result))
  // };
  const getUser = (email) => {
    axios.get(base_url + '/findUserByEmail/' + email, {

    }).then(res => {
      usersId = res.data.data.id;
      localStorage.setItem("uid", usersId);
      return usersId;
    })
  }
  const signIn = () =>{
    axios.post(base_url + '/v1/login',{
      usersId : getUser(email),
      email : email,
      password : pw
    },{
      headers: {
        'Content-Type': 'application/json',
      },
    withCredentials: true,
      crossDomain: true,
      credentials: "include"
    })
        .then(res => {
          if(res.status === 200){
            console.log(res.data)
            accessToken = res.data.data.accessToken
            localStorage.setItem("user", email)
            localStorage.setItem("token", res.data.data.accessToken)
            getUser(email)
            navigate('/')
          }else{
            alert('아이디 또는 비밀번호가 일치하지 않습니다.')
          }
        })
  }
  // const signIn = (e) =>{
  //   e.preventDefault();
  //   axios.post(base_url + '/v1/login',{
  //       email : email,
  //       password : pw
  //   },{
  //     headers: {
  //       'Content-Type': 'application/json'
  //     }
  //   })
  //       .then(res => res.status)
  //       .then(res => {
  //         if(res.config.data(email) === undefined){
  //           alert('입력하신 Email이 일치하지 않습니다.')
  //         }else if(res.data.email === null){
  //           alert('Email을 입력해주세요.')
  //         }else if(res.data.email === email){
  //           document.location.href = '/'
  //         }
  //       })
  // }

  // 회원가입 fetch
  const signUp = (e) => {
    e.preventDefault()
    fetch(base_url + '/v1/signUp', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email: rgEmail,
        password: rgPw,
        username : rgName,
        tel : rgTel
      }),
    })
      .then(res => res.json())
      .then(result => console.log('결과: ', result));
  };

  
	return ( 
		<>
    <div className="container" id="container" onLoad={Toggle}>
			<img src="/assets/Login/bg1.jpg"></img>

	    {/* 회원가입 */}
      <div className="row">
        <div className="col align-center flex-col sign-up">
          <div className="form-wrapper align-center">
            <form className="form sign-up">

            <div className="input-group">
            <i className="bx bx-mail-send"></i>
            <input
              type="email" 
              name="rgEmail"
              placeholder="Email"
              value={rgEmail}
              onChange={onRgEmailHandler}
            />

            </div>
            <div className="input-group">
              <i className="bx bxs-lock-alt"></i>
              <input
                type="password" 
                name ="rgPw"
                placeholder="Password"
                value={rgPw}
                onChange={onRgPwHandler}
                />
            </div>

              <div className="input-group">
                <i className="bx bxs-user"></i>
                <input
                 type="text" 
                 name="rgName"
                 placeholder="Username"
                 value={rgName}
                 onChange={onRgNameHandler}
                 />
              </div>

							<div className="input-group">
                <i className="bx bxs-phone"></i>
                <input 
                  type="tel"
                  name="rgTel"
                  placeholder="Tel" 
                  value={rgTel}
                  onChange={onRgTelHandler}
                  />
              </div>

              <button
                type="submit"
                onClick={signUp}
                >Sign up</button>
              <p>
                <span>Already have an account?</span>
                <b id="sign-in">&nbsp;Sign in here</b>
              </p>
            </form>
          </div>

		  {/* Icons */}
          <div className="form-wrapper">
            <div className="social-list align-center sign-up">
							<Icons></Icons>
            </div>
          </div>
        </div>

				{/* Sing-In */}
        <div className="col align-center flex-col sign-in">
          <div className="form-wrapper align-center">

            <div className="form sign-in">
            <div className="input-group">
            <i className="bx bx-mail-send"></i>
            <input
              type="email" 
              name="email"
              placeholder="Email"
              value={email}
              onChange={onEmailHandler}
            />

            </div>
            <div className="input-group">
              <i className="bx bxs-lock-alt"></i>
              <input
                type="password" 
                name ="password"
                placeholder="Password"
                value={pw}
                onChange={onPwHandler}
                />
            </div>

              <button
                type="submit"
                onSubmit={onSubmit}
                onClick={signIn}
              >Sign in</button>
              <p>
                <span> Don't have an account? </span>
                <b id="sign-up">&nbsp; Sign up here</b>
              </p>
            </div>
          </div>

				{/* Icons */}
          <div className="form-wrapper">
            <div className="social-list align-center sign-in">
							<Icons></Icons>
            </div>
          </div>
        </div>
      </div>
    </div>
		</>
	)
}

// icons
function Icons(){
	return(
		<>
    <div className="align-center home-bg">
    <i className='bx bxs-home' ></i>
    </div>   
		<div className="align-center facebook-bg">
			<i className="bx bxl-facebook"></i>
		</div>
		<div className="align-center google-bg">
			<i className="bx bxl-google"></i>
		</div>
		<div className="align-center twitter-bg">
			<i className="bx bxl-twitter"></i>
		</div>
		<div className="align-center insta-bg">
			<i className="bx bxl-instagram-alt"></i>
	  </div>
		</>
	)
}


export default Login;
