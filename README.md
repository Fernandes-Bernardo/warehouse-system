# Warehouse System (Almoxarifado)

Um sistema completo de gestão de estoque e almoxarifado, desenvolvido para controlar produtos, entradas, saídas, empréstimos e movimentações, com logs automáticos e interface moderna.

## Funcionalidades

- Gestão de Produtos
  - Cadastro, edição e exclusão de produtos.
  - Filtros por nome, categoria e origem.
  - Estoque mínimo configurável.
- Movimentações
  - Registro de entradas, saídas e empréstimos.
  - Controle de destino, responsável e quantidade.
- Logs do Sistema
  - Registro automático de todas as operações (CRUD).
  - Filtros por usuário, ação, entidade e intervalo de datas.
- Interface Moderna
  - Tabelas responsivas com paginação.
  - Filtros dinâmicos.
  - Modais animados para cadastros.
  - Botões flutuantes exclusivos para administradores.
- Segurança
  - Autenticação via JWT.
  - Permissões baseadas em roles (ADMIN / USER).

## Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- PostgreSQL (usado tanto em desenvolvimento quanto em produção)
- Cache implementado diretamente no PostgreSQL para otimizar consultas e reduzir latência — diferencial do sistema
- Maven

### Frontend
- React
- Vite
- TailwindCSS
- Heroicons
- Fetch API para integração com backend

### Outros
- Logs automáticos via LogService
- Componentização (Navbar, Footer, TabelaHeader)
- Deploy pronto para ambientes locais e cloud

## Como rodar o projeto

### Backend
```bash
# Entrar na pasta backend
cd almoxarifado_backend

# Rodar com Maven
./mvnw spring-boot:run
```

### Frontend
```bash
# Entrar na pasta frontend
cd almoxarifado_frontend

# Instalar dependências
npm install

# Rodar em modo dev
npm run dev
```

## Estrutura do Projeto
```
warehouse-system/
│
├── almoxarifado_backend/   # Backend Java + Spring Boot
│   ├── controller/
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── ...
│
├── almoxarifado_frontend/  # Frontend React + Vite + Tailwind
│   ├── src/
│   │   ├── components/
│   │   │   ├── Navbar.jsx
│   │   │   ├── Footer.jsx
│   │   │   └── TabelaHeader.jsx
│   │   ├── pages/
│   │   │   ├── Produtos.jsx
│   │   │   ├── Emprestimos.jsx
│   │   │   ├── Saidas.jsx
│   │   │   ├── Entradas.jsx
│   │   │   ├── Movimentacoes.jsx
│   │   │   └── Logs.jsx
│   │   └── ...
│   └── ...
│
└── README.md
```

## Autor
Desenvolvido por Bernardo Fernandes Pereira

## Licença
Este projeto é de uso livre para fins acadêmicos e profissionais.
