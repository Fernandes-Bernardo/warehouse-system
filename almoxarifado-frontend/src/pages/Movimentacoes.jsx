import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

export default function Movimentacoes() {
  const [movimentacoes, setMovimentacoes] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [tamanhoPagina] = useState(10);
  const [totalPaginas, setTotalPaginas] = useState(0);
  const [filtro, setFiltro] = useState({
    tipo: '',
    produtoId: '',
    responsavel: '',
    destino: '',
    periodo: '',
    dataInicio: '',
    dataFim: '',
  });
  const [showFiltros, setShowFiltros] = useState(false);

  useEffect(() => {
    buscarMovimentacoes();
  }, [pagina]);

  async function buscarMovimentacoes() {
    const params = new URLSearchParams();

    if (filtro.tipo) params.append('tipo', filtro.tipo);
    if (filtro.produtoId) params.append('produtoId', filtro.produtoId);
    if (filtro.responsavel) params.append('responsavel', filtro.responsavel);
    if (filtro.destino) params.append('destino', filtro.destino);
    if (filtro.periodo) params.append('periodo', filtro.periodo);
    if (filtro.dataInicio && filtro.dataFim) {
      params.append('dataInicio', filtro.dataInicio);
      params.append('dataFim', filtro.dataFim);
    }

    params.append('page', pagina);
    params.append('size', tamanhoPagina);

    try {
      const response = await fetch(`http://localhost:8080/api/v1/movimentacoes?${params.toString()}`);
      const data = await response.json();
      setMovimentacoes(data.content || []);
      setTotalPaginas(data.totalPages || 0);
    } catch (err) {
      console.error('Erro ao buscar movimentações:', err);
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-r from-preto via-vermelhoEscuro/40 to-rosaClaro/20 text-white flex flex-col">
      <Navbar />

      {/* Botão de filtro */}
      <div className="flex justify-end max-w-8xl mx-auto w-[95%] mt-6">
        <button
          className="flex items-center gap-2 bg-vermelho text-white px-4 py-2 rounded-md hover:bg-vermelhoEscuro transition shadow-md"
          onClick={() => setShowFiltros(!showFiltros)}
        >
          <span>Filtrar</span>
        </button>
      </div>

      {/* Painel de filtros */}
      {showFiltros && (
        <div className="max-w-8xl mx-auto w-[95%] bg-white p-6 rounded-md shadow-md mt-4 border border-vermelhoEscuro text-preto">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <select
              value={filtro.tipo}
              onChange={(e) => setFiltro({ ...filtro, tipo: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            >
              <option value="">Filtrar por tipo</option>
              <option value="ENTRADA">Entrada</option>
              <option value="RETIRADA">Retirada</option>
            </select>
            <input
              type="text"
              placeholder="Filtrar por ID do produto"
              value={filtro.produtoId}
              onChange={(e) => setFiltro({ ...filtro, produtoId: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <input
              type="text"
              placeholder="Filtrar por responsável"
              value={filtro.responsavel}
              onChange={(e) => setFiltro({ ...filtro, responsavel: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <input
              type="text"
              placeholder="Filtrar por destino"
              value={filtro.destino}
              onChange={(e) => setFiltro({ ...filtro, destino: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <select
              value={filtro.periodo}
              onChange={(e) => setFiltro({ ...filtro, periodo: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            >
              <option value="">Filtrar por período</option>
              <option value="semana">Última semana</option>
              <option value="mes">Último mês</option>
              <option value="ano">Último ano</option>
            </select>
            <input
              type="date"
              value={filtro.dataInicio}
              onChange={(e) => setFiltro({ ...filtro, dataInicio: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
            <input
              type="date"
              value={filtro.dataFim}
              onChange={(e) => setFiltro({ ...filtro, dataFim: e.target.value })}
              className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
            />
          </div>
          <button
            onClick={() => {
              setPagina(0);
              buscarMovimentacoes();
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
          <div className="overflow-x-auto">
            <table className="w-full table-fixed text-sm border-collapse">
              <thead>
                <tr className="bg-vermelhoEscuro text-white">
                  <th className="px-4 py-3 text-left">Data/Hora</th>
                  <th className="px-4 py-3 text-left">Tipo</th>
                  <th className="px-4 py-3 text-left">Responsável</th>
                  <th className="px-4 py-3 text-left">Quantidade</th>
                  <th className="px-4 py-3 text-left">Destino</th>
                  <th className="px-4 py-3 text-left">Produto</th>
                  <th className="px-4 py-3 text-left">Categoria</th>
                  <th className="px-4 py-3 text-left">Prateleira</th>
                  <th className="px-4 py-3 text-left">Origem</th>
                  <th className="px-4 py-3 text-left">Estoque Atual</th>
                  <th className="px-4 py-3 text-left">Estoque Mínimo</th>
                </tr>
              </thead>
              <tbody>
                {movimentacoes.length === 0 ? (
                  <tr>
                    <td colSpan="11" className="px-6 py-4 text-center text-vermelhoClaro">
                      Nenhuma movimentação encontrada.
                    </td>
                  </tr>
                ) : (
                  movimentacoes.map((m, index) => (
                    <tr
                      key={index}
                      className={`${
                        m.tipo === 'ENTRADA' ? 'bg-green-50' : 'bg-red-50'
                      } hover:bg-zinc-100 transition`}
                    >
                      <td className="px-4 py-4">
                        {new Date(m.dataHora).toLocaleString('pt-BR', {
                          day: '2-digit',
                          month: '2-digit',
                          year: 'numeric',
                          hour: '2-digit',
                          minute: '2-digit',
                        })}
                      </td>
                      <td className="px-4 py-4 font-bold text-sm text-white">
                        <span
                          className={`px-2 py-1 rounded ${
                            m.tipo === 'ENTRADA' ? 'bg-green-600' : 'bg-red-600'
                          }`}
                        >
                          {m.tipo}
                        </span>
                      </td>
                      <td className="px-4 py-4">{m.responsavel}</td>
                      <td className="px-4 py-4">{m.quantidade}</td>
                      <td className="px-4 py-4">{m.destino || '—'}</td>
                      <td className="px-4 py-4">{m.produtoNome}</td>
                      <td className="px-4 py-4">{m.produtoCategoria}</td>
                      <td className="px-4 py-4">{m.produtoPrateleira}</td>
                      <td className="px-4 py-4">{m.produtoOrigem}</td>
                      <td className="px-4 py-4">{m.produtoEstoqueAtual}</td>
                      <td className="px-4 py-4">
                        {m.produtoEstoqueMinimo}
                        {m.estoqueBaixo && (
                          <span className="ml-2 text-red-600 font-bold">⚠️</span>
                        )}
                      </td>
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
      <Footer />
    </div>
  );
}
