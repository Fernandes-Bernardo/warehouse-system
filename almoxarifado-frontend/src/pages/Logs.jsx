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

export default function Logs() {
    const [logs, setLogs] = useState([]);
    const [pagina, setPagina] = useState(0);
    const [totalPaginas, setTotalPaginas] = useState(0);
    const [filtro, setFiltro] = useState({
        usuario: '',
        acao: '',
        entidade: '',
        dataInicio: '',
        dataFim: '',
    });

    useEffect(() => {
        buscarLogs();
    }, [pagina]);

    async function buscarLogs() {
        const params = new URLSearchParams();
        if (filtro.usuario) params.append('usuario', filtro.usuario);
        if (filtro.acao) params.append('acao', filtro.acao);
        if (filtro.entidade) params.append('entidade', filtro.entidade);
        if (filtro.dataInicio && filtro.dataFim) {
            params.append('dataInicio', `${filtro.dataInicio}T00:00:00`);
            params.append('dataFim', `${filtro.dataFim}T23:59:59`);
        }
        params.append('page', pagina);
        params.append('size', 10);

        try {
            const token = localStorage.getItem('authToken');
            const response = await fetch(`http://localhost:8080/api/v1/logs?${params.toString()}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const data = await response.json();
            setLogs(data.content || []);
            setTotalPaginas(data.totalPages || 0);
        } catch (err) {
            console.error('Erro ao buscar logs:', err);
        }
    }

    return (
        <div className="min-h-screen bg-gradient-to-r from-preto via-vermelhoEscuro/40 to-rosaClaro/20 text-white flex flex-col">
            <Navbar />

            <div className="max-w-8xl mx-auto w-[95%] mt-10 bg-white rounded-xl shadow-xl border border-vermelhoEscuro text-preto px-4 sm:px-8 py-6">
                <h2 className="text-xl font-bold mb-4">Logs do Sistema</h2>

                {/* Filtros */}
                <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 mb-6">
                    <input
                        type="text"
                        placeholder="Filtrar por usuário"
                        value={filtro.usuario}
                        onChange={(e) => setFiltro({ ...filtro, usuario: e.target.value })}
                        className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                    />
                    <input
                        type="text"
                        placeholder="Filtrar por ação"
                        value={filtro.acao}
                        onChange={(e) => setFiltro({ ...filtro, acao: e.target.value })}
                        className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                    />
                    <input
                        type="text"
                        placeholder="Filtrar por entidade"
                        value={filtro.entidade}
                        onChange={(e) => setFiltro({ ...filtro, entidade: e.target.value })}
                        className="px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho"
                    />
                    <div className="flex gap-2">
                        <input
                            type="date"
                            value={filtro.dataInicio}
                            onChange={(e) => setFiltro({ ...filtro, dataInicio: e.target.value })}
                            className="px-2 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho flex-1"
                        />
                        <input
                            type="date"
                            value={filtro.dataFim}
                            onChange={(e) => setFiltro({ ...filtro, dataFim: e.target.value })}
                            className="px-2 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho flex-1"
                        />
                    </div>
                </div>

                <button
                    onClick={() => { setPagina(0); buscarLogs(); }}
                    className="mb-6 bg-vermelho text-white px-4 py-2 rounded-md hover:bg-vermelhoEscuro transition"
                >
                    Aplicar filtros
                </button>

                {/* Tabela */}
                <div className="overflow-x-auto">
                    <table className="w-full table-fixed text-sm border-collapse">
                        <thead>
                            <tr className="bg-red-800 text-white">
                                <th className="px-4 py-3 text-left">Data/Hora</th>
                                <th className="px-4 py-3 text-left">Usuário</th>
                                <th className="px-4 py-3 text-left">Ação</th>
                                <th className="px-4 py-3 text-left">Entidade</th>
                                <th className="px-4 py-3 text-left">Descrição</th>
                            </tr>
                        </thead>
                        <tbody>
                            {logs.length === 0 ? (
                                <tr>
                                    <td colSpan="5" className="px-6 py-4 text-center text-vermelhoClaro">
                                        Nenhum log encontrado.
                                    </td>
                                </tr>
                            ) : (
                                logs.map((log) => (
                                    <tr key={log.id} className="hover:bg-zinc-100 transition">
                                        <td className="px-4 py-4">{formatarDataHora(log.dataHora)}</td>
                                        <td className="px-4 py-4">{log.usuario}</td>
                                        <td className="px-4 py-4">{log.acao}</td>
                                        <td className="px-4 py-4">{log.entidade}</td>
                                        <td className="px-4 py-4">{log.descricao}</td>
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
            <Footer />
        </div>
    );
}