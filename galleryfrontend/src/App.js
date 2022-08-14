import './App.css';
import Login from './components/Login/login';
import Main from './components/Main/main';
import './components/Login/login.css'
import './components/Main/main.css'
import { Routes, Route } from 'react-router-dom'


function App() {
  return (
    <div className="App">
      <Routes>
        <Route exact path="/" element={<Main></Main>}></Route>
        <Route path="/login" element={<Login></Login>}></Route>
      </Routes>
    </div>
  );
}

export default App;
