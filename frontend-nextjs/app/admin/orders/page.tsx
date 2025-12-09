'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Eye, Truck, CheckCircle } from 'lucide-react';

// Define the possible order statuses
type OrderStatus = 'Processing' | 'Shipped' | 'Delivered' | 'Pending';

// Define the type for an order
interface Order {
  id: string;
  customer: string;
  date: string;
  total: number;
  status: OrderStatus;
}

// Mock data for orders
const mockOrders: Order[] = [
  { id: 'ORD-001', customer: 'John Doe', date: '2023-10-26', total: 125.50, status: 'Processing' },
  { id: 'ORD-002', customer: 'Jane Smith', date: '2023-10-25', total: 89.99, status: 'Shipped' },
  { id: 'ORD-003', customer: 'Bob Johnson', date: '2023-10-25', total: 340.00, status: 'Delivered' },
  { id: 'ORD-004', customer: 'Alice Williams', date: '2023-10-24', total: 45.00, status: 'Pending' },
  { id: 'ORD-005', customer: 'Charlie Brown', date: '2023-10-23', total: 199.99, status: 'Delivered' },
];

const statusColors: Record<OrderStatus, string> = {
  Processing: 'bg-yellow-100 text-yellow-800',
  Shipped: 'bg-blue-100 text-blue-800',
  Delivered: 'bg-green-100 text-green-800',
  Pending: 'bg-gray-100 text-gray-800',
};

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>(mockOrders);

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Orders</h1>
        <p className="text-gray-500">Track and manage customer orders</p>
      </header>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <table className="w-full text-left">
          <thead>
            <tr className="border-b bg-gray-50">
              <th className="p-3">Order ID</th>
              <th className="p-3">Customer</th>
              <th className="p-3">Date</th>
              <th className="p-3">Total</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {orders.map(order => (
              <motion.tr 
                key={order.id} 
                className="border-b hover:bg-gray-50"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <td className="p-3 font-medium">{order.id}</td>
                <td className="p-3 text-gray-600">{order.customer}</td>
                <td className="p-3 text-gray-600">{order.date}</td>
                <td className="p-3 text-gray-600">${order.total.toFixed(2)}</td>
                <td className="p-3">
                  <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[order.status]}`}>
                    {order.status}
                  </span>
                </td>
                <td className="p-3 flex items-center space-x-2">
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-blue-500"><Eye size={18} /></motion.button>
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-yellow-500"><Truck size={18} /></motion.button>
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-green-500"><CheckCircle size={18} /></motion.button>
                </td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
