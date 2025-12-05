'use client';

import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from 'recharts';

// Mock data - replace with API call
const initialMetrics = {
  totalUsers: 1250,
  pendingOrders: 75,
  totalSales: 89000.50,
  topSellingProducts: [
    { name: 'iPhone 14 Pro', sales: 450 },
    { name: 'Samsung Galaxy S23', sales: 380 },
    { name: 'MacBook Pro 16"', sales: 320 },
    { name: 'Sony WH-1000XM5', sales: 280 },
    { name: 'iPad Air', sales: 210 },
  ],
};

// Card component for displaying a single metric
function MetricCard({ title, value, unit, icon }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="bg-white p-6 rounded-lg shadow-md flex items-center space-x-4"
    >
      <div className="text-3xl text-blue-500">{icon}</div>
      <div>
        <p className="text-gray-500 text-sm">{title}</p>
        <p className="text-2xl font-bold">
          {unit === '$' ? `${unit}${value}` : `${value} ${unit}`}
        </p>
      </div>
    </motion.div>
  );
}

export default function DashboardPage() {
  const [metrics, setMetrics] = useState(initialMetrics);
  const [isLoading, setIsLoading] = useState(true);

  // Fetch data from API
  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setIsLoading(false);
    }, 1000);
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Admin Dashboard</h1>
        <p className="text-gray-500">Resumen del rendimiento de la tienda</p>
      </header>

      {isLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="bg-white p-6 rounded-lg shadow-md animate-pulse">
              <div className="h-8 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div className="h-12 bg-gray-200 rounded w-1/2"></div>
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <MetricCard title="Total Users" value={metrics.totalUsers} unit="" icon="👥" />
          <MetricCard title="Pending Orders" value={metrics.pendingOrders} unit="" icon="📦" />
          <MetricCard title="Total Sales" value={metrics.totalSales.toFixed(2)} unit="$" icon="💰" />
        </div>
      )}

      <div className="mt-8">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">Top 5 Selling Products</h2>
        <div className="bg-white p-6 rounded-lg shadow-md">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={metrics.topSellingProducts}>
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="sales" fill="#3B82F6" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}
