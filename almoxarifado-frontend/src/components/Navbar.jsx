import {
  CubeIcon,
  ClipboardDocumentListIcon,
  ArrowTrendingUpIcon,
  DocumentChartBarIcon,
} from '@heroicons/react/24/solid';
import { useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { isAdmin } from '../utils/authUtils';

export default function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  const currentPath = location.pathname;

  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <nav className="bg-zinc-900 text-white px-10 py-10 shadow-xl flex items-center justify-between">
      {/* Logo à esquerda */}
      <div className="flex items-center gap-3">
        <CubeIcon className="h-8 w-8 text-rosaClaro" />
        <span className="text-3xl font-extrabold tracking-wide">Almoxarifado</span>
      </div>

      {/* Links à direita */}
      <ul className="flex gap-12 text-lg font-medium tracking-wide relative">
        {/* Produtos */}
        <li
          onClick={() => navigate('/produtos')}
          className={`flex items-center gap-2 cursor-pointer transition ${
            currentPath === '/produtos'
              ? 'text-rosaClaro border-b-2 border-rosaClaro pb-1'
              : 'hover:text-rosaClaro'
          }`}
        >
          <ClipboardDocumentListIcon className="h-5 w-5" />
          Produtos
        </li>

        {/* Movimentações com dropdown */}
        <div
          className="relative"
          onMouseEnter={() => setShowDropdown(true)}
          onMouseLeave={() => setShowDropdown(false)}
        >
          <div
            onClick={() => navigate('/movimentacoes')}
            className={`flex items-center gap-2 cursor-pointer transition ${
              currentPath === '/movimentacoes'
                ? 'text-rosaClaro border-b-2 border-rosaClaro pb-1'
                : 'hover:text-rosaClaro'
            }`}
          >
            <ArrowTrendingUpIcon className="h-5 w-5" />
            Movimentações
          </div>

          {/* Dropdown */}
          <ul
            className={`absolute top-full left-0 mt-2 w-52 bg-zinc-900 text-white shadow-lg z-50 border border-zinc-700 transition-all duration-200 ease-out transform origin-top ${
              showDropdown ? 'opacity-100 scale-100 visible' : 'opacity-0 scale-95 invisible'
            }`}
          >
            <li
              onClick={() => navigate('/entradas')}
              className="px-4 py-2 hover:bg-zinc-800 cursor-pointer"
            >
              Entradas
            </li>
            <li
              onClick={() => navigate('/saidas')}
              className="px-4 py-2 hover:bg-zinc-800 cursor-pointer"
            >
              Saídas
            </li>
            <li
              onClick={() => navigate('/emprestimos')}
              className="px-4 py-2 hover:bg-zinc-800 cursor-pointer"
            >
              Empréstimos
            </li>
          </ul>
        </div>

        {/* Logs (somente admin) */}
        {isAdmin() && (
          <li
            onClick={() => navigate('/logs')}
            className={`flex items-center gap-2 cursor-pointer transition ${
              currentPath === '/logs'
                ? 'text-rosaClaro border-b-2 border-rosaClaro pb-1'
                : 'hover:text-rosaClaro'
            }`}
          >
            <DocumentChartBarIcon className="h-5 w-5" />
            Logs
          </li>
        )}
      </ul>
    </nav>
  );
}