import type { Metadata, Viewport } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'E‑Commerce Pro · Marketplace 3D premium',
  description:
    'Plataforma de e‑commerce estilo Apple × Tesla con visualización 3D avanzada y experiencia de compra premium.',
  keywords: ['ecommerce', 'marketplace', '3D', 'productos', 'premium'],
  authors: [{ name: 'E‑Commerce Pro' }],
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
  maximumScale: 1,
  viewportFit: 'cover',
  themeColor: '#FFFFFF',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es">
      <body className="antialiased">{children}</body>
    </html>
  );
}
