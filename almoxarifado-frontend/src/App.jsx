import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Produtos from './pages/Produtos';
import Login from './pages/Login';

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/produtos" element={<Produtos />} />
      </Routes>
    </Router>
  );
}