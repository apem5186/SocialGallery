import Toggle from "../js/toggle";

function Login() {
	
	return ( 
		<>
    <div className="container" id="container" onLoad={Toggle}>
			{/* Background Img */}
			<img src="/assets/Login/bg1.jpg"></img>

	    {/* Sing-Up */}
      <div className="row">
        <div className="col align-center flex-col sign-up">
          <div className="form-wrapper align-center">
            <form className="form sign-up">
							<Sign></Sign>
              <div className="input-group">
                <i className="bx bxs-user"></i>
                <input type="text" placeholder="Username" />
              </div>
							<div className="input-group">
                <i className="bx bxs-phone"></i>
                <input type="tel" placeholder="Tel" />
              </div>
              <button>Sign up</button>
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
							<Sign></Sign>
              <button>Sign in</button>
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
	)
}

function Icons(){
	return(
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
	)
}

function Sign(){
	return(
		<>              
		<div className="input-group">
		<i className="bx bx-mail-send"></i>
		<input type="email" placeholder="Email" />
		</div>
		<div className="input-group">
			<i className="bx bxs-lock-alt"></i>
			<input type="password" placeholder="Password" />
		</div>
		</>
	)
}

export default Login;