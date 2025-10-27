// src/components/Footer.jsx
import { HeartIcon, ChatBubbleBottomCenterTextIcon, ShieldCheckIcon } from '@heroicons/react/24/solid';

export default function Footer() {
  return (
    <footer className="bg-zinc-900 py-8 text-center text-sm text-rosaClaro/70 mt-10">
      <div className="flex justify-center gap-6 mb-4">
        <HeartIcon className="h-5 w-5 text-rosaClaro/70" />
        <ChatBubbleBottomCenterTextIcon className="h-5 w-5 text-rosaClaro/70" />
        <ShieldCheckIcon className="h-5 w-5 text-rosaClaro/70" />
        <a
          href="https://github.com/Fernandes-Bernardo/warehouse-system"
          target="_blank"
          rel="noopener noreferrer"
          className="inline-flex items-center hover:opacity-80 transition"
          title="Abrir repositório no GitHub"
        >
          {/* Ícone GitHub (SVG inline) */}
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20" height="20" viewBox="0 0 24 24"
            fill="currentColor" className="text-rosaClaro/80"
          >
            <path d="M12 .5C5.73.5.77 5.46.77 11.73c0 4.92 3.19 9.09 7.62 10.57.56.1.77-.25.77-.55 0-.27-.01-1.16-.02-2.11-3.1.67-3.76-1.32-3.76-1.32-.51-1.29-1.25-1.63-1.25-1.63-1.02-.7.08-.69.08-.69 1.13.08 1.73 1.17 1.73 1.17 1 .1 2.03.74 2.52.57.1-.73.39-1.24.71-1.53-2.48-.28-5.08-1.24-5.08-5.52 0-1.22.43-2.21 1.14-2.99-.11-.28-.49-1.42.11-2.97 0 0 .93-.3 3.05 1.14.88-.24 1.83-.36 2.77-.36s1.89.12 2.77.36c2.12-1.44 3.05-1.14 3.05-1.14.6 1.55.22 2.69.11 2.97.71.78 1.14 1.77 1.14 2.99 0 4.29-2.6 5.23-5.08 5.52.4.35.75 1.03.75 2.09 0 1.51-.01 2.72-.01 3.09 0 .3.21.66.78.55 4.43-1.48 7.62-5.64 7.62-10.56C23.23 5.46 18.27.5 12 .5z"/>
          </svg>
        </a>
      </div>
      <p className="text-xs tracking-wide">
        © 2025 Almoxarifado — Desenvolvido por <span className="text-rosaClaro">Nexus</span>
      </p>
    </footer>
  );
}