import { useState } from "react";
import Toggle from "./toggle";
import  {Link} from 'react-router-dom'


function Login() {
	// 로그인 Form
  const [email,setEmail] = useState('');
  const [pw,setPw] = useState('');

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

  // 로그인 fetch
  const signIn = (e) => {
    e.preventDefault()
    fetch('', { 
      method: 'POST',
      body: JSON.stringify({
        email: email,
        password: pw,
      }),
    })
      .then(res => res.json())
      .then(result => console.log('결과', result))
  };

  // 회원가입 fetch
  const signUp = (e) => {
    e.preventDefault()
    fetch('/user/signUp', {
      method: 'POST',
      body: JSON.stringify({
        email: rgEmail,
        password: rgPw,
        name : rgName,
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
