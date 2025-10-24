import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Produtos from './pages/Produtos';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/produtos" element={<Produtos />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;