import './App.css';
import './components/PostReg/upload.css'
import Login from './components/Login/login';
import Main from './components/Main/main';
import './components/Login/login.css'
import './components/Main/main.css'
import { Routes, Route } from 'react-router-dom'
import UpLoad from './components/PostReg/upload';
import Movie from "./components/SideMenu/Movie";
import Broadcast from "./components/SideMenu/Broadcast";
import Life from "./components/SideMenu/Life";
import Travel from "./components/SideMenu/Travel";


function App() {
    

    return (
        <div className="App">
            <Routes>
                {/* Main */}
                <Route exact path="/" element={<Main ></Main>}></Route>
                <Route path="/:pid" element={<Main></Main>}></Route>
                <Route path="/upload" element={<UpLoad></UpLoad>}></Route>

                {/* Login */}
                <Route path="/login/" element={<Login></Login>}></Route>
                <Route path="/login/:location" element={<Login></Login>}></Route>

                 {/*SideMenu*/}
                    <Route path="/MOVIE" element={<Movie></Movie>}></Route>
                    <Route path="/BROADCAST" element={<Broadcast></Broadcast>}></Route>
                <Route path="/LIFE" element={<Life></Life>}></Route>
                <Route path="/TRAVEL" element={<Travel></Travel>}></Route>
            </Routes>
        </div>
    );
}

export default App;