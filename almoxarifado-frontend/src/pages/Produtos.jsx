import {
  FunnelIcon,
  HeartIcon,
  ChatBubbleBottomCenterTextIcon,
  ShieldCheckIcon,
} from '@heroicons/react/24/solid';
import { useEffect, useState } from 'react';
import TabelaHeader from '../components/TabelaHeader';
import Navbar from '../components/Navbar';

export default function Produtos() {
  const [produtos, setProdutos] = useState([]);
  const [filtro, setFiltro] = useState({ nome: '', categoria: '', origem: '' });
  const [showFiltros, setShowFiltros] = useState(false);
  const [pagina, setPagina] = useState(0);
  const [tamanhoPagina, setTamanhoPagina] = useState(10);
  const [totalPaginas, setTotalPaginas] = useState(0);

  useEffect(() => {
    buscarProdutos();
  }, [pagina]);

  async function buscarProdutos() {
    const params = new URLSearchParams();

    if (filtro.nome) params.append('nome', filtro.nome);
    if (filtro.categoria) params.append('categoria', filtro.categoria);
    if (filtro.origem) params.append('origem', filtro.origem);

    params.append('page', pagina);
    params.append('size', tamanhoPagina);

    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/produtos?${params.toString()}`
      );
      const data = await response.json();
      setProdutos(data.content || []);
      setTotalPaginas(data.totalPages || 0);
    } catch (err) {
      console.error('Erro ao buscar produtos:', err);
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-r from-preto via-vermelhoEscuro/40 to-rosaClaro/20 text-white flex flex-col">
      <Navbar />

      {/* Botão de filtro - entre navbar e tabela */}
      <div className="flex justify-end max-w-8xl mx-auto w-[95%] mt-6">
        <button
          className="flex items-center gap-2 bg-vermelho text-white px-4 py-2 rounded-md hover:bg-vermelhoEscuro transition shadow-md"
          onClick={() => setShowFiltros(!showFiltros)}
        >
          <FunnelIcon className="h-5 w-5" />
          <span>Filtrar</span>
        </button>
      </div>

      {/* Painel de filtros */}
      {showFiltros && (
        <div className="max-w-8xl mx-auto w-[95%] bg-white p-6 rounded-md shadow-md mt-4 border border-vermelhoEscuro text-preto">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <input
              type="text"
              placeholder="Filtrar por nome"
              value={filtro.nome}
              onChange={(e) => setFiltro({ ...filtro, nome: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <input
              type="text"
              placeholder="Filtrar por categoria"
              value={filtro.categoria}
              onChange={(e) => setFiltro({ ...filtro, categoria: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <input
              type="text"
              placeholder="Filtrar por origem"
              value={filtro.origem}
              onChange={(e) => setFiltro({ ...filtro, origem: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
          </div>
          <button
            onClick={() => {
              setPagina(0);
              buscarProdutos();
            }}
            className="mt-4 bg-vermelho text-white px-4 py-2 rounded-md hover:bg-vermelhoEscuro transition"
          >
            Aplicar filtros
          </button>
        </div>
      )}

      {/* Tabela */}
      <div className="flex-grow p-4 sm:p-6 md:p-8 max-w-8xl mx-auto w-[95%] animate-fade-in">
        <div className="bg-white rounded-xl shadow-xl border border-vermelhoEscuro text-preto px-4 sm:px-8 py-6 w-full min-h-[70vh]">

          {/* Cabeçalho com sombra e bordas alinhadas */}
          <div className="-mx-8 -mt-6 overflow-hidden rounded-t-xl shadow-md">
            <TabelaHeader />
          </div>

          <div className="overflow-x-auto">
            <table className="w-full table-fixed text-sm border-collapse">
              <tbody>
                {produtos.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-6 py-4 text-center text-vermelhoClaro">
                      Nenhum produto encontrado.
                    </td>
                  </tr>
                ) : (
                  produtos.map((produto, index) => (
                    <tr
                      key={produto.id}
                      className={`${
                        index % 2 === 0 ? 'bg-white' : 'bg-rosaClaro/30'
                      } hover:bg-rosaClaro/50 transition`}
                    >
                      <td className="px-4 py-4 w-24">{produto.id}</td>
                      <td className="px-4 py-4 w-48">{produto.nome}</td>
                      <td className="px-4 py-4 w-32">{produto.categoria}</td>
                      <td className="px-4 py-4 w-32">{produto.prateleira}</td>
                      <td className="px-4 py-4 w-32">{produto.origem}</td>
                      <td className="px-4 py-4 w-24">{produto.quantidade}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {/* Paginação */}
          <div className="flex justify-center items-center gap-2 py-6">
            {Array.from({ length: totalPaginas }, (_, i) => (
              <button
                key={i}
                onClick={() => setPagina(i)}
                className={`px-3 py-2 rounded-md border ${
                  pagina === i
                    ? 'bg-vermelhoEscuro text-white border-vermelho'
                    : 'bg-white text-vermelho border-vermelhoClaro hover:bg-rosaClaro'
                }`}
              >
                {i + 1}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Rodapé fixo profissional */}
      <footer className="bg-zinc-900 py-8 text-center text-sm text-rosaClaro/70 mt-10">
        <div className="flex justify-center gap-6 mb-4">
          <HeartIcon className="h-5 w-5 text-rosaClaro/70" />
          <ChatBubbleBottomCenterTextIcon className="h-5 w-5 text-rosaClaro/70" />
          <ShieldCheckIcon className="h-5 w-5 text-rosaClaro/70" />
        </div>
        <p className="text-xs tracking-wide">
          © 2025 Almoxarifado — Desenvolvido por <span className="text-rosaClaro">Nexus</span>
        </p>
      </footer>
    </div>
  );
}
