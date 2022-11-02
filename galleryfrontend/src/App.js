import './App.css';
import './components/PostReg/upload.css'
import Login from './components/Login/login';
import Main from './components/Main/main';
import './components/Login/login.css'
import './components/Main/main.css'
import { Routes, Route } from 'react-router-dom'
import UpLoad from './components/PostReg/upload';
import Movie from "./components/SideMenu/movie";
import Life from "./components/SideMenu/life";
import Travel from "./components/SideMenu/travel";
import Broadcast from "./components/SideMenu/broadcast";


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
                    <Route path="/movie" element={<Movie></Movie>}></Route>
                    <Route path="/broadcast" element={<Broadcast></Broadcast>}></Route>
                <Route path="/life" element={<Life></Life>}></Route>
                <Route path="/travel" element={<Travel></Travel>}></Route>
            </Routes>
        </div>
    );
}

export default App;