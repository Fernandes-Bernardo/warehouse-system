import { LockClosedIcon } from '@heroicons/react/24/solid';
import { login } from '../services/authService';
import { EyeIcon, EyeSlashIcon } from '@heroicons/react/24/solid';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

  async function handleLogin(e) {
    e.preventDefault();

    const username = e.target[0].value;
    const password = e.target[1].value;

    try {
      const data = await login(username, password);
      console.log('Token recebido:', data.accessToken);

      localStorage.setItem('authToken', data.accessToken);
      localStorage.setItem('userRole', data.role);

      navigate('/produtos');
    } catch (err) {
      console.error('Erro no login:', err.message);
    }
  }

  return (
    <div className="relative min-h-screen flex items-center justify-center bg-gradient-to-r from-preto via-vermelhoEscuro/40 to-rosaClaro/20 overflow-hidden">
      {/* Fundo*/}
      <div className="absolute w-96 h-96 bg-vermelho rounded-full blur-3xl opacity-30 -top-20 -left-20"></div>
      <div className="absolute w-72 h-72 bg-vermelhoEscuro rounded-full blur-2xl opacity-20 -bottom-10 -right-10"></div>

      {/* Caixa do formulário */}
      <div className="bg-rosaClaro p-8 rounded-2xl shadow-xl w-full max-w-md z-10 transform transition duration-300 hover:scale-[1.02]">
        <div className="flex flex-col items-center mb-6">
          <LockClosedIcon className="h-10 w-10 text-vermelho mb-2 animate-bounce" />
          <h2 className="text-3xl font-bold text-vermelho">Bem-vindo de volta</h2>
          <p className="text-sm text-vermelhoClaro mt-1">Acesse o sistema com seus dados</p>
        </div>

        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <label className="block text-sm font-medium text-vermelho mb-1">Usuário</label>
            <input
              type="text"
              placeholder="Digite seu usuário"
              className="w-full px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho bg-white"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-vermelho mb-1">Senha</label>
            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="Digite sua senha"
                className="w-full px-4 py-2 border border-vermelhoClaro rounded-md focus:outline-none focus:ring-2 focus:ring-vermelho bg-white pr-10"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-2 top-2 text-vermelho hover:text-vermelhoEscuro"
              >
                {showPassword ? (
                  <EyeSlashIcon className="h-5 w-5" />
                ) : (
                  <EyeIcon className="h-5 w-5" />
                )}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-vermelho text-white py-2 rounded-md hover:bg-vermelhoEscuro transition"
          >
            Entrar
          </button>

          <div className="text-center mt-2">
            <a href="#" className="text-sm text-vermelho hover:underline">
              Esqueceu a senha?
            </a>
          </div>
        </form>
      </div>
    </div>
  );
}