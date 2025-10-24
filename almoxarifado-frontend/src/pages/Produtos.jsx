import {
  FunnelIcon,
  HeartIcon,
  ChatBubbleBottomCenterTextIcon,
  ShieldCheckIcon,
  PlusIcon,
} from '@heroicons/react/24/solid';
import { useEffect, useState } from 'react';
import TabelaHeader from '../components/TabelaHeader';
import Navbar from '../components/Navbar';
import { isAdmin } from '../utils/authUtils';

export default function Produtos() {
  const [produtos, setProdutos] = useState([]);
  const [filtro, setFiltro] = useState({ nome: '', categoria: '', origem: '' });
  const [showFiltros, setShowFiltros] = useState(false);
  const [pagina, setPagina] = useState(0);
  const [tamanhoPagina] = useState(10);
  const [totalPaginas, setTotalPaginas] = useState(0);

  // Modal
  const [modalOpen, setModalOpen] = useState(false);
  const [closing, setClosing] = useState(false);

  // Formulário
  const [form, setForm] = useState({
    nome: '',
    descricao: '',
    categoria: '',
    prateleira: '',
    origem: '',
    quantidade: '',
    estoqueMinimo: '',
  });

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

  // Handlers do formulário
  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token'); // JWT salvo no login
      const response = await fetch("http://localhost:8080/api/v1/produtos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
        body: JSON.stringify({
          nome: form.nome,
          descricao: form.descricao,
          categoria: form.categoria,
          prateleira: form.prateleira,
          origem: form.origem,
          quantidade: Number(form.quantidade),
          estoqueMinimo: Number(form.estoqueMinimo),
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Erro ao salvar produto: ${response.status} - ${errorText}`);
      }

      setForm({
        nome: '',
        descricao: '',
        categoria: '',
        prateleira: '',
        origem: '',
        quantidade: '',
        estoqueMinimo: '',
      });
      closeModal();
      setPagina(0);
      buscarProdutos();
    } catch (err) {
      console.error('Erro ao salvar produto:', err);
      alert('Erro ao salvar produto. Verifique se você está logado como ADMIN.');
    }
  }

  function openModal() {
    setModalOpen(true);
    setClosing(false);
  }

  function closeModal() {
    setClosing(true);
  }

  function handleAnimationEnd() {
    if (closing) {
      setModalOpen(false);
      setClosing(false);
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
                      className={`${index % 2 === 0 ? 'bg-white' : 'bg-rosaClaro/30'
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
                className={`px-3 py-2 rounded-md border ${pagina === i
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

      {/* Footer */}
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

      {/* Botão flutuante de adicionar produto */}
      {isAdmin() && (
        <button
          onClick={openModal}
          className="fixed bottom-3 right-3 bg-vermelho hover:bg-vermelhoEscuro text-white w-16 h-16 rounded-full shadow-lg flex items-center justify-center transition z-50"
          title="Cadastrar Produto"
        >
          <PlusIcon className="h-8 w-8" />
        </button>
      )}

      {/* Modal de cadastro */}
      {modalOpen && (
        <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50">
          <div
            onAnimationEnd={handleAnimationEnd}
            className={`relative bg-white text-preto rounded-xl shadow-2xl p-8 w-full max-w-lg transform transition-all ${closing ? 'animate-fadeOutDown' : 'animate-fadeInUp'
              }`}
          >
            {/* Botão de fechar */}
            <button
              onClick={closeModal}
              className="absolute top-4 right-4 text-vermelho hover:text-vermelhoEscuro font-bold text-xl"
            >
              ×
            </button>

            <h2 className="text-2xl font-bold text-vermelho mb-6 text-center">
              Registrar Produto
            </h2>

            {/* Formulário */}
            <form className="space-y-4" onSubmit={handleSubmit}>
              <div>
                <label className="block text-sm font-semibold mb-1">Nome</label>
                <input
                  type="text"
                  name="nome"
                  value={form.nome}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite o nome do produto"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Descrição</label>
                <textarea
                  name="descricao"
                  value={form.descricao}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite a descrição do produto"
                  rows={3}
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Categoria</label>
                <input
                  type="text"
                  name="categoria"
                  value={form.categoria}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite a categoria"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Prateleira</label>
                <input
                  type="text"
                  name="prateleira"
                  value={form.prateleira}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite a prateleira"
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Origem</label>
                <input
                  type="text"
                  name="origem"
                  value={form.origem}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite a origem"
                />
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold mb-1">Quantidade</label>
                  <input
                    type="number"
                    name="quantidade"
                    value={form.quantidade}
                    onChange={handleChange}
                    className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                    placeholder="Digite a quantidade"
                    min="0"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">Estoque mínimo</label>
                  <input
                    type="number"
                    name="estoqueMinimo"
                    value={form.estoqueMinimo}
                    onChange={handleChange}
                    className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                    placeholder="Quantidade mínima em estoque"
                    min="0"
                    required
                  />
                </div>
              </div>

              <div className="flex items-center gap-3 pt-2">
                <button
                  type="submit"
                  className="flex-1 bg-vermelho hover:bg-vermelhoEscuro text-white font-semibold py-2 rounded-md transition shadow-md"
                >
                  Salvar Produto
                </button>
                <button
                  type="button"
                  onClick={closeModal}
                  className="flex-1 bg-zinc-200 hover:bg-zinc-300 text-preto font-semibold py-2 rounded-md transition"
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}