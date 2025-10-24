import {
  CubeIcon,
  ClipboardDocumentListIcon,
  ArrowTrendingUpIcon,
  DocumentChartBarIcon,
} from '@heroicons/react/24/solid';
import { useLocation, useNavigate } from 'react-router-dom';

export default function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();

  const currentPath = location.pathname;

  const navItems = [
    {
      label: 'Produtos',
      icon: <ClipboardDocumentListIcon className="h-5 w-5" />,
      path: '/produtos',
    },
    {
      label: 'Movimentações',
      icon: <ArrowTrendingUpIcon className="h-5 w-5" />,
      path: '/movimentacoes',
    },
    {
      label: 'Relatórios',
      icon: <DocumentChartBarIcon className="h-5 w-5" />,
      path: '/relatorios',
    },
  ];

  return (
    <nav className="bg-zinc-900 text-white px-10 py-10 shadow-xl flex items-center justify-between">
      {/* Logo à esquerda */}
      <div className="flex items-center gap-3">
        <CubeIcon className="h-8 w-8 text-rosaClaro" />
        <span className="text-3xl font-extrabold tracking-wide">Almoxarifado</span>
      </div>

      {/* Links à direita */}
      <ul className="flex gap-12 text-lg font-semibold tracking-wide">
        {navItems.map((item) => (
          <li
            key={item.path}
            onClick={() => navigate(item.path)}
            className={`flex items-center gap-2 cursor-pointer transition ${
              currentPath === item.path
                ? 'text-rosaClaro border-b-2 border-rosaClaro pb-1'
                : 'hover:text-rosaClaro'
            }`}
          >
            {item.icon}
            {item.label}
          </li>
        ))}
      </ul>
    </nav>
  );
}