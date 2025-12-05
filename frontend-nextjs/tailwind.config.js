/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        background: '#FFFFFF',
        foreground: '#1D1D1F',
        'apple-blue': '#007AFF',
        'apple-blue-hover': '#0A84FF',
        'apple-gray': '#F5F5F7',
        'apple-border': '#E5E5E7',
        'apple-text-secondary': '#86868B',
      },
      borderRadius: {
        lg: '12px',
        xl: '12px',
        '2xl': '12px',
      },
      boxShadow: {
        sm: '0 1px 3px rgba(0,0,0,0.06)',
        md: '0 4px 12px rgba(0,0,0,0.08)',
        lg: '0 8px 24px rgba(0,0,0,0.12)',
      },
    },
  },
  plugins: [],
};

