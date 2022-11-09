import './App.css';
import './components/postReg/upload.css'
import Login from './components/login/Login';
import Main from './components/main/Main';
import './components/login/login.css'
import './components/main/main.css'
import { Routes, Route } from 'react-router-dom'
import Upload from './components/postReg/Upload';
import Movie from "./components/sideMenu/Movie";
import Life from "./components/sideMenu/Life";
import Travel from "./components/sideMenu/Travel";
import Broadcast from "./components/sideMenu/Broadcast";


function App() {
    

    return (
        <div className="App">
            <Routes>
                {/* Main */}
                <Route exact path="/" element={<Main ></Main>}></Route>
                <Route path="/:pid" element={<Main></Main>}></Route>
                <Route path="/upload" element={<Upload></Upload>}></Route>

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