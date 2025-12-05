'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Search, Briefcase, CheckCircle, XCircle } from 'lucide-react';

// Define the possible seller statuses
type SellerStatus = 'Approved' | 'Pending' | 'Rejected';

// Define the type for a seller
interface Seller {
  id: number;
  name: string;
  owner: string;
  email: string;
  status: SellerStatus;
  registered: string;
}

// Mock data for sellers
const mockSellers: Seller[] = [
  { id: 1, name: 'Gadget Store', owner: 'Seller One', email: 'seller1@example.com', status: 'Approved', registered: '2023-03-01' },
  { id: 2, name: 'Fashion Hub', owner: 'Seller Two', email: 'seller2@example.com', status: 'Approved', registered: '2023-04-05' },
  { id: 3, name: 'Book Nook', owner: 'Seller Three', email: 'seller3@example.com', status: 'Pending', registered: '2023-10-20' },
  { id: 4, name: 'Home Goods Co.', owner: 'Seller Four', email: 'seller4@example.com', status: 'Rejected', registered: '2023-09-15' },
];

const statusStyles: Record<SellerStatus, string> = {
  Approved: "bg-green-100 text-green-800",
  Pending: "bg-yellow-100 text-yellow-800",
  Rejected: "bg-red-100 text-red-800",
};

export default function SellersPage() {
  const [sellers, setSellers] = useState<Seller[]>(mockSellers);
  const [searchTerm, setSearchTerm] = useState('');

  const filteredSellers = sellers.filter(seller =>
    seller.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    seller.owner.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <header className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Sellers</h1>
          <p className="text-gray-500">Manage seller accounts and applications</p>
        </div>
      </header>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="flex items-center mb-4">
          <Search className="text-gray-400 mr-2" />
          <input 
            type="text"
            placeholder="Search by store or owner..."
            className="w-full p-2 border border-gray-200 rounded-lg"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <table className="w-full text-left">
          <thead>
            <tr className="border-b bg-gray-50">
              <th className="p-3">Store Name</th>
              <th className="p-3">Owner</th>
              <th className="p-3">Email</th>
              <th className="p-3">Registered</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredSellers.map(seller => (
              <motion.tr 
                key={seller.id} 
                className="border-b hover:bg-gray-50"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <td className="p-3 font-medium">{seller.name}</td>
                <td className="p-3 text-gray-600">{seller.owner}</td>
                <td className="p-3 text-gray-600">{seller.email}</td>
                <td className="p-3 text-gray-600">{seller.registered}</td>
                <td className="p-3">
                  <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusStyles[seller.status]}`}>
                    {seller.status}
                  </span>
                </td>
                <td className="p-3 flex items-center space-x-2">
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-blue-500"><Briefcase size={18} /></motion.button>
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-green-500"><CheckCircle size={18} /></motion.button>
                  <motion.button whileHover={{ scale: 1.1 }} className="p-1 text-gray-500 hover:text-red-500"><XCircle size={18} /></motion.button>
                </td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
