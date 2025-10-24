import {
  CubeIcon,
  ClipboardDocumentListIcon,
  ArrowTrendingUpIcon,
  DocumentChartBarIcon,
} from '@heroicons/react/24/solid';

export default function Navbar() {
  return (
    <nav className="bg-zinc-900 text-white px-10 py-10 shadow-xl flex items-center justify-between">
      {/* Logo à esquerda */}
      <div className="flex items-center gap-3">
        <CubeIcon className="h-8 w-8 text-rosaClaro" />
        <span className="text-3xl font-extrabold tracking-wide">Almoxarifado</span>
      </div>

      {/* Links à direita */}
      <ul className="flex gap-12 text-lg font-semibold tracking-wide">
        <li className="flex items-center gap-2 hover:text-rosaClaro cursor-pointer transition">
          <ClipboardDocumentListIcon className="h-5 w-5" />
          Produtos
        </li>
        <li className="flex items-center gap-2 hover:text-rosaClaro cursor-pointer transition">
          <ArrowTrendingUpIcon className="h-5 w-5" />
          Movimentações
        </li>
        <li className="flex items-center gap-2 hover:text-rosaClaro cursor-pointer transition">
          <DocumentChartBarIcon className="h-5 w-5" />
          Relatórios
        </li>
      </ul>
    </nav>
  );
}