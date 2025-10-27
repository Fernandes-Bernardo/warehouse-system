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
    <thead className="bg-vermelhoEscuro text-white text-sm font-semibold uppercase tracking-wide shadow-md">
      <tr>
        {colunas.map((coluna, index) => (
          <th
            key={index}
            className={`px-4 py-3 text-left whitespace-nowrap ${
              index === 0
                ? 'w-24'
                : index === 1
                ? 'w-48'
                : index === 2 || index === 3 || index === 4
                ? 'w-32'
                : 'w-24'
            }`}
          >
            {coluna.icone || null}
            {coluna.nome}
          </th>
        ))}
      </tr>
    </thead>
  );
}
