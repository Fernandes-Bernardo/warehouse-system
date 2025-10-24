import { TagIcon } from '@heroicons/react/24/solid';

export default function TabelaHeader() {
  const colunas = [
    { nome: 'Código', icone: <TagIcon className="h-4 w-4 inline mr-1" /> },
    { nome: 'Nome' },
    { nome: 'Categoria' },
    { nome: 'Localização' },
    { nome: 'Origem' },
    { nome: 'Quantidade' },
  ];

  return (
    <table className="w-full border-collapse shadow-md">
      <thead className="bg-vermelhoEscuro text-white text-sm font-semibold uppercase tracking-wide shadow-md">
        <tr>
          {colunas.map((coluna, index) => (
            <th key={index} className="px-4 py-3 text-left whitespace-nowrap">
              {coluna.icone || null}
              {coluna.nome}
            </th>
          ))}
        </tr>
      </thead>
    </table>
  );
}
