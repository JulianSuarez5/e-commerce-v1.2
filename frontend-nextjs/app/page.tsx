'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import AppleHeader from '@/components/AppleHeader';
import ProductCard from '@/components/ProductCard';
import { ProductDto } from '@/types/product';
import { Container } from '@/ui/container';
import { Button } from '@/ui/button';
import { HeroOrbitModel } from '@/components/HeroOrbitModel';

export default function Home() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch('/api/products');
      const data = await response.json();
      setProducts(data);
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      {/* Hero Section - Estilo Apple Minimalista */}
      <section className="pt-32 pb-24">
        <Container>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-10 items-center">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, ease: [0.4, 0, 0.2, 1] }}
              className="max-w-xl"
            >
              <h1 className="text-display mb-6">
                Descubre productos en 3D
              </h1>
              <p className="text-subtitle mb-8">
                Experiencia de compra inmersiva con visualización interactiva.
                Explora cada detalle antes de comprar.
              </p>
              <Button asChild>
                <Link href="/products">Explorar productos</Link>
              </Button>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.7, ease: [0.4, 0, 0.2, 1], delay: 0.1 }}
            >
              <HeroOrbitModel />
            </motion.div>
          </div>
        </Container>
      </section>

      {/* Products Grid - Estilo Apple Clean */}
      <section className="py-16 bg-[#F5F5F7]">
        <Container>
          <h2 className="text-title mb-12 text-center">
            Productos Destacados
          </h2>
          
          {loading ? (
            <div className="text-center text-body text-[#86868B] py-16">
              Cargando productos...
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {products.slice(0, 8).map((product, index) => (
                <motion.div
                  key={product.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ 
                    duration: 0.4, 
                    delay: index * 0.1,
                    ease: [0.4, 0, 0.2, 1]
                  }}
                >
                  <ProductCard product={product} />
                </motion.div>
              ))}
            </div>
          )}
        </Container>
      </section>

      {/* Features Section - Estilo Apple */}
      <section className="py-24 bg-[#FFFFFF]">
        <Container>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.4 }}
              className="text-center"
            >
              <div className="w-16 h-16 bg-[#F5F5F7] rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">🎮</span>
              </div>
              <h3 className="text-title mb-2 text-[#1D1D1F] text-base">
                Visualización 3D
              </h3>
              <p className="text-body text-[#86868B]">
                Explora productos en tres dimensiones antes de comprar
              </p>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.4, delay: 0.1 }}
              className="text-center"
            >
              <div className="w-16 h-16 bg-[#F5F5F7] rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">🚀</span>
              </div>
              <h3 className="text-title mb-2 text-[#1D1D1F] text-base">
                Envío Rápido
              </h3>
              <p className="text-body text-[#86868B]">
                Recibe tus productos en tiempo récord
              </p>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.4, delay: 0.2 }}
              className="text-center"
            >
              <div className="w-16 h-16 bg-[#F5F5F7] rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">⭐</span>
              </div>
              <h3 className="text-title mb-2 text-[#1D1D1F] text-base">
                Calidad Garantizada
              </h3>
              <p className="text-body text-[#86868B]">
                Productos verificados y de alta calidad
              </p>
            </motion.div>
          </div>
        </Container>
      </section>
    </div>
  );
}
