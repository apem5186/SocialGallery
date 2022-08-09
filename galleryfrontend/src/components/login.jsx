import { useState } from "react";
import Toggle from "../js/toggle";
import { call } from "../service/ApiService";

function Login() {
  //baseUrl
  const baseUrl = "http://localhost:8080";
  // 로그인 Form
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");

  const onEmailHandler = (e) => {
    setEmail(e.currentTarget.value);
  };
  const onPwHandler = (e) => {
    setPw(e.currentTarget.value);
  };
  const onSubmit = (e) => {
    e.preventDefault();
  };

  // 회원가입 Form
  const [rgEmail, setRgEmail] = useState(""); // rg = Register
  const [rgPw, setRgPw] = useState("");
  const [rgName, setRgName] = useState("");
  const [rgTel, setRgTel] = useState("");

  const onRgEmailHandler = (e) => {
    setRgEmail(e.currentTarget.value);
  };

  const onRgPwHandler = (e) => {
    setRgPw(e.currentTarget.value);
  };

  const onRgNameHandler = (e) => {
    setRgName(e.currentTarget.value);
  };
  const onRgTelHandler = (e) => {
    setRgTel(e.currentTarget.value);
  };

  const onRgSubmit = (event) => {
    event.preventDefault();
  };
  // 로그인 fetch
  const signIn = () => {
    fetch(baseUrl+"/user/signIn", {
      method: "POST",
      headers: {
        "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email: email,
        password: pw,
      }),
    })
      .then((res) => res.json())
      .then((result) => console.log("결과", result));
  };

  // const signUp = {
  //   body: JSON.stringify({
  //     email: rgEmail,
  //     password: rgPw,
  //     username: rgName,
  //     phone: rgTel,
  //   }),
  // };
  // call("/user/signUp", "POST", signUp).then(console.log(signUp));

  // 회원가입 fetch
  const signUp = () => {
    fetch(baseUrl+"/user/signUp", {
      method: "POST",
      mode: "cors",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email: rgEmail,
        password: rgPw,
        username: rgName,
        phone: rgTel,
      }),
    })
      .then((res) => res.json())
      .then((result) => console.log("결과: ", result));
  };

  return (
    <>
      <div className="container" id="container" onLoad={Toggle}>
        {/* Background Img */}
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
                    name="rgPw"
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

                <button type="submit" onSubmit={onRgSubmit} onClick={signUp}>
                  Sign up
                </button>
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
                    name="password"
                    placeholder="Password"
                    value={pw}
                    onChange={onPwHandler}
                  />
                </div>

                <button type="submit" onSubmit={onSubmit} onClick={signIn}>
                  Sign in
                </button>
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

        {/*	  여백 Text 
      <div className="row content-row">
        <div className="col align-items flex-col">
          <div className="text sign-in">
            <h2></h2>
            <p></p>
          </div>
        </div>
        <div className="col align-items fl ex-col">
          <div className="text sign-up">
            <h2></h2>
          </div>
        </div>
      </div>
*/}
      </div>
    </>
  );
}

function Icons() {
  return (
    <>
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
  );
}

export default Login;
