import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Produtos from './pages/Produtos';
import Entradas from './pages/Entradas';
import Saidas from './pages/Saidas';
import Movimentacoes from './pages/Movimentacoes';
import Emprestimos from './pages/Emprestimos';
import Logs from './pages/Logs';
import Login from './pages/Login';

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/produtos" element={<Produtos />} />
        <Route path="/entradas" element={<Entradas />} />
        <Route path="/saidas" element={<Saidas />} />
        <Route path="/movimentacoes" element={<Movimentacoes />} />
        <Route path="/emprestimos" element={<Emprestimos />} />
        <Route path="/logs" element={<Logs />} />
      </Routes>
    </Router>
  );
}