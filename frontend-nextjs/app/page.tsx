'use client';

import Link from 'next/link';
import { HeroOrbitModel } from '@/components/HeroOrbitModel';
import { Container } from '@/ui/container';
import { Section } from '@/ui/section';
import { Button } from '@/ui/button';
import { ArrowRight, Sparkles } from 'lucide-react';
import { motion } from 'framer-motion';

export default function HomePage() {
  return (
    <div className="min-h-screen bg-white">
      {/* Hero Section */}
      <Section className="relative pt-32 pb-20 overflow-hidden">
        <Container>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            {/* Text Content */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="space-y-8"
            >
              <div className="inline-flex items-center space-x-2 px-4 py-2 rounded-full bg-[#F5F5F7] border border-[#E5E5E7]">
                <Sparkles className="w-4 h-4 text-[#007AFF]" />
                <span className="text-sm text-[#86868B]">Marketplace Premium</span>
              </div>
              
              <h1 className="text-display text-[#1D1D1F]">
                Innovación a tu Alcance
              </h1>
              
              <p className="text-subtitle text-lg max-w-lg">
                Descubre una nueva era de compras online. Calidad y diseño premium en cada producto.
              </p>
              
              <div className="flex flex-col sm:flex-row gap-4">
                <Button asChild size="lg" className="group">
                  <Link href="/products">
                    Explorar Productos
                    <ArrowRight className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" />
                  </Link>
                </Button>
                <Button asChild variant="secondary" size="lg">
                  <Link href="/register">
                    Crear Cuenta
                  </Link>
                </Button>
              </div>
            </motion.div>

            {/* 3D Hero Model */}
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.8, delay: 0.2 }}
              className="relative"
            >
              <HeroOrbitModel />
            </motion.div>
          </div>
        </Container>
      </Section>

      {/* Features Section */}
      <Section className="bg-[#F5F5F7]">
        <Container>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {[
              {
                title: 'Productos Premium',
                description: 'Selección cuidadosa de productos de alta calidad',
                icon: '✨',
              },
              {
                title: 'Visualización 3D',
                description: 'Explora productos en 3D antes de comprar',
                icon: '🎨',
              },
              {
                title: 'Experiencia Premium',
                description: 'Diseño inspirado en Apple y Tesla',
                icon: '🚀',
              },
            ].map((feature, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className="text-center p-6 rounded-[16px] bg-white border border-[#E5E5E7]"
              >
                <div className="text-4xl mb-4">{feature.icon}</div>
                <h3 className="text-title text-lg text-[#1D1D1F] mb-2">
                  {feature.title}
                </h3>
                <p className="text-body text-[#86868B]">
                  {feature.description}
                </p>
              </motion.div>
            ))}
          </div>
        </Container>
      </Section>
    </div>
  );
}
