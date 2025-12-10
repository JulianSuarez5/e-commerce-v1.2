'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { PlusCircle, Search } from 'lucide-react';

// Mock data for products
const mockProducts = [
  { id: 1, name: 'iPhone 14 Pro', category: 'Electronics', price: 999, stock: 150, sales: 450 },
  { id: 2, name: 'Samsung Galaxy S23', category: 'Electronics', price: 899, stock: 200, sales: 380 },
  { id: 3, name: 'MacBook Pro 16"', category: 'Computers', price: 2499, stock: 80, sales: 320 },
  { id: 4, name: 'Sony WH-1000XM5', category: 'Audio', price: 399, stock: 300, sales: 280 },
  { id: 5, name: 'iPad Air', category: 'Tablets', price: 599, stock: 120, sales: 210 },
];

export default function ProductsPage() {
  const [products, setProducts] = useState(mockProducts);
  const [searchTerm, setSearchTerm] = useState('');

  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <header className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Products</h1>
          <p className="text-gray-500">Manage your product catalog</p>
        </div>
        <motion.button 
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          className="flex items-center bg-blue-500 text-white px-4 py-2 rounded-lg shadow-md"
        >
          <PlusCircle className="mr-2" size={20} />
          Add Product
        </motion.button>
      </header>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="flex items-center mb-4">
          <Search className="text-gray-400 mr-2" />
          <input 
            type="text"
            placeholder="Search for products..."
            className="w-full p-2 border border-gray-200 rounded-lg"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <table className="w-full text-left">
          <thead>
            <tr className="border-b bg-gray-50">
              <th className="p-3">Product</th>
              <th className="p-3">Category</th>
              <th className="p-3">Price</th>
              <th className="p-3">Stock</th>
              <th className="p-3">Sales</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredProducts.map(product => (
              <motion.tr 
                key={product.id} 
                className="border-b hover:bg-gray-50"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <td className="p-3 font-medium">{product.name}</td>
                <td className="p-3 text-gray-600">{product.category}</td>
                <td className="p-3 text-gray-600">${product.price}</td>
                <td className="p-3 text-gray-600">{product.stock}</td>
                <td className="p-3 text-gray-600">{product.sales}</td>
                <td className="p-3">
                  <button className="text-blue-500 hover:underline mr-4">Edit</button>
                  <button className="text-red-500 hover:underline">Delete</button>
                </td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
