import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import { isAdmin } from '../utils/authUtils';
import Footer from '../components/Footer';

function formatarDataHora(isoString) {
  if (!isoString) return '-';
  const data = new Date(isoString);
  return data.toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export default function Emprestimos() {
  const [emprestimos, setEmprestimos] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [form, setForm] = useState({
    produtoId: '',
    quantidadeEmprestada: '',
    responsavel: '',
    destino: '',
  });
  const [pagina, setPagina] = useState(0);
  const [totalPaginas, setTotalPaginas] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [closing, setClosing] = useState(false);

  useEffect(() => {
    buscarEmprestimos();
    buscarProdutos();
  }, [pagina]);

  function closeModal() {
    setClosing(true);
  }

  function handleAnimationEnd() {
    if (closing) {
      setModalOpen(false);
      setClosing(false);
    }
  }

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function buscarEmprestimos() {
    const params = new URLSearchParams();
    params.append('page', pagina);
    params.append('size', 10);

    try {
      const response = await fetch(`http://localhost:8080/api/v1/emprestimos?${params.toString()}`);
      const data = await response.json();
      setEmprestimos(data.content || []);
      setTotalPaginas(data.totalPages || 0);
    } catch (err) {
      console.error('Erro ao buscar empréstimos:', err);
    }
  }

  async function buscarProdutos() {
    try {
      const response = await fetch('http://localhost:8080/api/v1/produtos');
      const data = await response.json();
      setProdutos(data.content || []);
    } catch (err) {
      console.error('Erro ao buscar produtos:', err);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const token = localStorage.getItem('authToken');

    if (!token) {
      alert('Token não encontrado. Faça login novamente.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/v1/emprestimos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          produto: { id: Number(form.produtoId) },
          quantidadeEmprestada: Number(form.quantidadeEmprestada),
          responsavel: form.responsavel,
          destino: form.destino,
        }),
      });

      if (response.ok) {
        setForm({
          produtoId: '',
          quantidadeEmprestada: '',
          responsavel: '',
          destino: '',
        });
        closeModal();
        buscarEmprestimos();
      } else {
        alert('Erro ao registrar empréstimo.');
      }
    } catch (err) {
      console.error('Erro ao enviar empréstimo:', err);
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-r from-preto via-vermelhoEscuro/40 to-rosaClaro/20 text-white flex flex-col">
      <Navbar />

      {/* Tabela */}
      <div className="max-w-8xl mx-auto w-[95%] mt-10 bg-white rounded-xl shadow-xl border border-vermelhoEscuro text-preto px-4 sm:px-8 py-6">
        <div className="overflow-x-auto">
          <table className="w-full table-fixed text-sm border-collapse">
            <thead>
              <tr className="bg-red-800 text-white">
                <th className="px-4 py-3 text-left">Data/Hora</th>
                <th className="px-4 py-3 text-left">Responsável</th>
                <th className="px-4 py-3 text-left">Destino</th>
                <th className="px-4 py-3 text-left">Quantidade</th>
                <th className="px-4 py-3 text-left">Produto</th>
                <th className="px-4 py-3 text-left">Categoria</th>
                <th className="px-4 py-3 text-left">Prateleira</th>
                <th className="px-4 py-3 text-left">Origem</th>
              </tr>
            </thead>
            <tbody>
              {emprestimos.length === 0 ? (
                <tr>
                  <td colSpan="8" className="px-6 py-4 text-center text-vermelhoClaro">
                    Nenhum empréstimo encontrado.
                  </td>
                </tr>
              ) : (
                emprestimos.map((e) => (
                  <tr key={e.id} className="hover:bg-zinc-100 transition">
                    <td className="px-4 py-4">{formatarDataHora(e.dataHora)}</td>
                    <td className="px-4 py-4">{e.responsavel}</td>
                    <td className="px-4 py-4">{e.destino}</td>
                    <td className="px-4 py-4">{e.quantidadeEmprestada}</td>
                    <td className="px-4 py-4">{e.produtoNome}</td>
                    <td className="px-4 py-4">{e.produtoCategoria}</td>
                    <td className="px-4 py-4">{e.produtoPrateleira}</td>
                    <td className="px-4 py-4">{e.produtoOrigem}</td>
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
            {/* Botão flutuante*/}
      {isAdmin() && (
        <button
          onClick={() => setModalOpen(true)}
          className="fixed bottom-6 right-6 bg-vermelho text-white p-4 rounded-full shadow-lg hover:bg-vermelhoEscuro transition z-50"
        >
          Registrar Empréstimo
        </button>
      )}

      {/* Modal de cadastro */}
      {modalOpen && (
        <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50">
          <div
            onAnimationEnd={handleAnimationEnd}
            className={`relative bg-white text-preto rounded-xl shadow-2xl p-8 w-full max-w-lg transform transition-all ${
              closing ? 'animate-fadeOutDown' : 'animate-fadeInUp'
            }`}
          >
            <button
              onClick={closeModal}
              className="absolute top-4 right-4 text-vermelho hover:text-vermelhoEscuro font-bold text-xl"
            >
              ×
            </button>

            <h2 className="text-2xl font-bold text-vermelho mb-6 text-center">
              Registrar Empréstimo
            </h2>

            <form className="space-y-4" onSubmit={handleSubmit}>
              <div>
                <label className="block text-sm font-semibold mb-1">Produto</label>
                <select
                  name="produtoId"
                  value={form.produtoId}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md bg-white text-preto focus:outline-none focus:ring-2 focus:ring-vermelho shadow-sm font-medium"
                  required
                >
                  <option value="" disabled>
                    Selecione um produto
                  </option>
                  {produtos.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.id} — {p.nome}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Quantidade</label>
                <input
                  type="number"
                  name="quantidadeEmprestada"
                  value={form.quantidadeEmprestada}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Digite a quantidade"
                  min="1"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Responsável</label>
                <input
                  type="text"
                  name="responsavel"
                  value={form.responsavel}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Ex: Carlos Mendes - RA: 763182378"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-1">Destino</label>
                <input
                  type="text"
                  name="destino"
                  value={form.destino}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-zinc-300 rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                  placeholder="Ex: Laboratório de Informática"
                  required
                />
              </div>

              <div className="flex items-center gap-3 pt-2">
                <button
                  type="submit"
                  className="flex-1 bg-vermelho hover:bg-vermelhoEscuro text-white font-semibold py-2 rounded-md transition shadow-md"
                >
                  Salvar Empréstimo
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
      <Footer />
    </div>
  );
}