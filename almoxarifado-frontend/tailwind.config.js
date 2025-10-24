/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        preto: "#000000",
        vermelhoEscuro: "#8B0000",
        vermelho: "#C20000",
        vermelhoClaro: "#E57A7A",
        rosaClaro: "#FCEAEA",
      },
      keyframes: {
        fadeIn: {
          "0%": { opacity: 0, transform: "translateY(10px)" },
          "100%": { opacity: 1, transform: "translateY(0)" },
        },
        fadeInUp: {
          "0%": { opacity: 0, transform: "translateY(8px) scale(0.98)" },
          "100%": { opacity: 1, transform: "translateY(0) scale(1)" },
        },
        fadeOutDown: {
          "0%": { opacity: 1, transform: "translateY(0) scale(1)" },
          "100%": { opacity: 0, transform: "translateY(8px) scale(0.98)" },
        },
      },
      animation: {
        fadeIn: "fadeIn 0.4s ease-out",
        fadeInUp: "fadeInUp 250ms ease-out forwards",
        fadeOutDown: "fadeOutDown 250ms ease-in forwards",
      },
    },
  },
  plugins: [],
};