import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import { isAdmin } from '../utils/authUtils';
import Footer from '../components/Footer';

export default function Saidas() {
    const [saidas, setSaidas] = useState([]);
    const [pagina, setPagina] = useState(0);
    const [tamanhoPagina] = useState(10);
    const [totalPaginas, setTotalPaginas] = useState(0);
    const [filtro, setFiltro] = useState({
        produtoId: '',
        nome: '',
        responsavel: '',
        destino: '',
        periodo: '',
        dataInicio: '',
        dataFim: '',
    });
    const [showFiltros, setShowFiltros] = useState(false);

    // Modal
    const [modalOpen, setModalOpen] = useState(false);
    const [closing, setClosing] = useState(false);

    // Formulário
    const [form, setForm] = useState({
        produtoId: '',
        quantidadeRetirada: '',
        responsavel: '',
        destino: '',
    });

    // Produtos para dropdown
    const [produtos, setProdutos] = useState([]);

    useEffect(() => {
        buscarSaidas();
        buscarProdutos();
    }, [pagina]);

    async function buscarSaidas() {
        const params = new URLSearchParams();

        if (filtro.produtoId) params.append('produtoId', filtro.produtoId);
        if (filtro.nome) params.append('nome', filtro.nome);
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
            const response = await fetch(`http://localhost:8080/api/v1/retiradas?${params.toString()}`);
            const data = await response.json();
            setSaidas(data.content || []);
            setTotalPaginas(data.totalPages || 0);
        } catch (err) {
            console.error('Erro ao buscar saídas:', err);
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

    function handleChange(e) {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    }

    async function handleSubmit(e) {
        e.preventDefault();

        const token = localStorage.getItem('authToken');
        if (!token) {
            alert('Token não encontrado. Faça login novamente.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/retiradas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    produto: { id: Number(form.produtoId) },
                    quantidadeRetirada: Number(form.quantidadeRetirada),
                    responsavel: form.responsavel,
                    destino: form.destino,
                }),
            });

            if (response.ok) {
                alert('Saída registrada com sucesso!');
                setModalOpen(false);
                buscarSaidas();
            } else {
                alert('Erro ao registrar saída.');
            }
        } catch (err) {
            console.error('Erro ao enviar saída:', err);
        }
    }

    function closeModal() {
        setClosing(true);
    }

    function handleAnimationEnd() {
        if (closing) {
            setClosing(false);
            setModalOpen(false);
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
                        <input
                            type="text"
                            placeholder="Filtrar por ID do produto"
                            value={filtro.produtoId}
                            onChange={(e) => setFiltro({ ...filtro, produtoId: e.target.value })}
                            className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                        />
                        <input
                            type="text"
                            placeholder="Filtrar por nome do produto"
                            value={filtro.nome}
                            onChange={(e) => setFiltro({ ...filtro, nome: e.target.value })}
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
                            buscarSaidas();
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
                            <thead className="bg-vermelho text-white text-sm font-semibold uppercase tracking-wide">
                                <tr>
                                    <th className="px-4 py-3 text-left">ID</th>
                                    <th className="px-4 py-3 text-left">Data/Hora</th>
                                    <th className="px-4 py-3 text-left">Quantidade</th>
                                    <th className="px-4 py-3 text-left">Responsável</th>
                                    <th className="px-4 py-3 text-left">Destino</th>
                                    <th className="px-4 py-3 text-left">Produto</th>
                                    <th className="px-4 py-3 text-left">Categoria</th>
                                    <th className="px-4 py-3 text-left">Prateleira</th>
                                    <th className="px-4 py-3 text-left">Origem</th>
                                </tr>
                            </thead>
                            <tbody>
                                {saidas.length === 0 ? (
                                    <tr>
                                        <td colSpan="9" className="px-6 py-4 text-center text-vermelhoClaro">
                                            Nenhuma saída encontrada.
                                        </td>
                                    </tr>
                                ) : (
                                    saidas.map((saida, index) => (
                                        <tr
                                            key={saida.id}
                                            className={`${index % 2 === 0 ? 'bg-white' : 'bg-rosaClaro/30'
                                                } hover:bg-rosaClaro/50 transition`}
                                        >
                                            <td className="px-4 py-4">{saida.id}</td>
                                            <td className="px-4 py-4">
                                                {new Date(saida.dataHora).toLocaleString('pt-BR', {
                                                    day: '2-digit',
                                                    month: '2-digit',
                                                    year: 'numeric',
                                                    hour: '2-digit',
                                                    minute: '2-digit',
                                                })}
                                            </td>
                                            <td className="px-4 py-4">{saida.quantidadeRetirada}</td>
                                            <td className="px-4 py-4">{saida.responsavel}</td>
                                            <td className="px-4 py-4">{saida.destino}</td>
                                            <td className="px-4 py-4">{saida.produtoNome}</td>
                                            <td className="px-4 py-4">{saida.produtoCategoria}</td>
                                            <td className="px-4 py-4">{saida.produtoPrateleira}</td>
                                            <td className="px-4 py-4">{saida.produtoOrigem}</td>
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

            {/* Botão flutuante de registrar saída (somente ADMIN) */}
            {isAdmin() && (
                <button
                    onClick={() => setModalOpen(true)}
                    className="fixed bottom-6 right-6 bg-vermelho text-white p-4 rounded-full shadow-lg hover:bg-vermelhoEscuro transition z-50"
                >
                    Registrar Saída
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
                        <button
                            onClick={closeModal}
                            className="absolute top-4 right-4 text-vermelho hover:text-vermelhoEscuro font-bold text-xl"
                        >
                            ×
                        </button>

                        <h2 className="text-2xl font-bold text-vermelho mb-6 text-center">
                            Registrar Saída
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
                                    name="quantidadeRetirada"
                                    value={form.quantidadeRetirada}
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
                                    placeholder="Ex: Gabriel - RA: 938213"
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
                                    placeholder="Ex: Sala de Aula 1"
                                    required
                                />
                            </div>

                            <div className="flex items-center gap-3 pt-2">
                                <button
                                    type="submit"
                                    className="flex-1 bg-vermelho hover:bg-vermelhoEscuro text-white font-semibold py-2 rounded-md transition shadow-md"
                                >
                                    Salvar Saída
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
