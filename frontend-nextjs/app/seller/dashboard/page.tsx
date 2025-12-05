'use client';

import ProtectedRoute from '@/components/ProtectedRoute';
import AppleHeader from '@/components/AppleHeader';
import { motion } from 'framer-motion';
import { Package, DollarSign, TrendingUp, Users } from 'lucide-react';
import { Container } from '@/ui/container';
import { Card } from '@/ui/card';
import { Button } from '@/ui/button';

export default function SellerDashboardPage() {
  return (
    <ProtectedRoute requiredRole="SELLER">
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />

        <Container className="pt-24 pb-16">
          <h1 className="text-display mb-8">
            Dashboard de Vendedor
          </h1>

          {/* Stats Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className=""
            >
              <Card className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <Package className="w-8 h-8 text-[#007AFF]" />
                  <span className="text-xs text-[#86868B]">Productos</span>
                </div>
                <p className="text-title text-2xl text-[#1D1D1F]">0</p>
              </Card>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 }}
              className=""
            >
              <Card className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <DollarSign className="w-8 h-8 text-[#007AFF]" />
                  <span className="text-xs text-[#86868B]">Ventas</span>
                </div>
                <p className="text-title text-2xl text-[#1D1D1F]">$0.00</p>
              </Card>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
              className=""
            >
              <Card className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <TrendingUp className="w-8 h-8 text-[#007AFF]" />
                  <span className="text-xs text-[#86868B]">Crecimiento</span>
                </div>
                <p className="text-title text-2xl text-[#1D1D1F]">0%</p>
              </Card>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
              className=""
            >
              <Card className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <Users className="w-8 h-8 text-[#007AFF]" />
                  <span className="text-xs text-[#86868B]">Clientes</span>
                </div>
                <p className="text-title text-2xl text-[#1D1D1F]">0</p>
              </Card>
            </motion.div>
          </div>

          {/* Quick Actions */}
          <Card className="p-6">
            <h2 className="text-title text-lg text-[#1D1D1F] mb-6">
              Acciones rápidas
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <Button variant="secondary">Agregar producto</Button>
              <Button variant="secondary">Ver órdenes</Button>
              <Button variant="secondary">Configuración</Button>
            </div>
          </Card>
        </Container>
      </div>
    </ProtectedRoute>
  );
}

