// tailwind.config.js
module.exports = {
  content: [
    './src/**/*.{html,ts,css}',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Arial', 'sans-serif'],
      },
      colors: {
        'magenta-900': '#1e1a1d',
        'fuchsia-600': '#c026d3',
        'fuchsia-700': '#a71cc1',
        'fuchsia-500': '#ec4899',
        'green-100': '#f0fdf4',
        'green-500': '#10b981',
        'green-700': '#059669',
        'red-600': '#ef4444',
        'red-700': '#dc2626',
      },
    },
  },
  plugins: [],
}
