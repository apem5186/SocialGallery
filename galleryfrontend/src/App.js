import './App.css';
import './components/PostReg/upload.css'
import Login from './components/Login/login';
import Main from './components/Main/main';
import './components/Login/login.css'
import './components/Main/main.css'
import { Routes, Route } from 'react-router-dom'
import UpLoad from './components/PostReg/upload';
import Test from './components/Main/test';



function App() {
  return (
    <div className="App">
      <Routes>
        <Route exact path="/" element={<Main></Main>}></Route>
        <Route path="/login" element={<Login></Login>}></Route>
        <Route path="/upload" element={<UpLoad></UpLoad>}></Route>
        <Route path="/test" element={<Test></Test>}></Route>
      </Routes>
    </div>
  );
}

export default App;
