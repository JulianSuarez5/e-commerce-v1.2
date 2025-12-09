/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  images: {
    domains: ['localhost', '127.0.0.1', 'images.unsplash.com'],
    remotePatterns: [
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8081',
        pathname: '/uploads/**',
      },
    ],
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: `${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081'}/api/:path*`,
      },
    ];
  },
  webpack: (config) => {
    // Ignorar archivos de sistema de Windows
    if (!config.watchOptions) config.watchOptions = {};
    config.watchOptions.ignored = [
      '**/node_modules/**',
      '**/.git/**',
      '**/C:\\pagefile.sys',
      '**/C:\\hiberfil.sys',
      '**/C:\\swapfile.sys',
      '**/C:\\DumpStack.log.tmp',
    ];
    return config;
  },
};

module.exports = nextConfig;
