'use client';

import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';
import { ArrowUpRight, Package, ShoppingBag, Users } from 'lucide-react';
import { motion } from 'framer-motion';

export default function AdminHomePage() {
  return (
    <div className="space-y-8">
      <header>
        <h1 className="text-display mb-2">Panel general</h1>
        <p className="text-subtitle">
          Vista rápida del rendimiento del marketplace.
        </p>
      </header>

      {/* Metric cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.35, ease: [0.4, 0, 0.2, 1] }}
        >
          <Card className="p-5 flex items-center justify-between">
            <div>
              <p className="text-xs text-[#86868B] mb-1">Ventas de hoy</p>
              <p className="text-title text-2xl">$0.00</p>
            </div>
            <ShoppingBag className="w-8 h-8 text-[#007AFF]" />
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.35, ease: [0.4, 0, 0.2, 1], delay: 0.05 }}
        >
          <Card className="p-5 flex items-center justify-between">
            <div>
              <p className="text-xs text-[#86868B] mb-1">Órdenes activas</p>
              <p className="text-title text-2xl">0</p>
            </div>
            <Package className="w-8 h-8 text-[#007AFF]" />
          </Card>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.35, ease: [0.4, 0, 0.2, 1], delay: 0.1 }}
        >
          <Card className="p-5 flex items-center justify-between">
            <div>
              <p className="text-xs text-[#86868B] mb-1">Usuarios registrados</p>
              <p className="text-title text-2xl">0</p>
            </div>
            <Users className="w-8 h-8 text-[#007AFF]" />
          </Card>
        </motion.div>
      </div>

      {/* Recent orders table (placeholder) */}
      <Card className="p-5">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-title text-base mb-1">Órdenes recientes</h2>
            <p className="text-xs text-[#86868B]">
              Lista simulada a la espera de conectar con la API de órdenes.
            </p>
          </div>
          <button className="inline-flex items-center gap-1 text-xs text-[#007AFF] hover:opacity-80">
            Ver todas
            <ArrowUpRight className="w-3 h-3" />
          </button>
        </div>
        <Table>
          <THead>
            <TR>
              <TH>Número</TH>
              <TH>Cliente</TH>
              <TH>Estado</TH>
              <TH className="text-right">Total</TH>
            </TR>
          </THead>
          <TBody>
            <TR>
              <TD colSpan={4} className="text-center text-xs text-[#86868B] py-6">
                Aún no hay datos conectados. Integra esta tabla con la API de
                órdenes cuando esté disponible.
              </TD>
            </TR>
          </TBody>
        </Table>
      </Card>
    </div>
  );
}


