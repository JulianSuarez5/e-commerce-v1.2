'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Search, UserPlus } from 'lucide-react';

// Mock data for users
const mockUsers = [
  { id: 1, name: 'John Doe', email: 'john.doe@example.com', role: 'Customer', joined: '2023-01-15' },
  { id: 2, name: 'Jane Smith', email: 'jane.smith@example.com', role: 'Customer', joined: '2023-02-20' },
  { id: 3, name: 'Admin User', email: 'admin@example.com', role: 'Admin', joined: '2022-11-10' },
  { id: 4, name: 'Seller One', email: 'seller1@example.com', role: 'Seller', joined: '2023-03-01' },
  { id: 5, name: 'Seller Two', email: 'seller2@example.com', role: 'Seller', joined: '2023-04-05' },
];

export default function UsersPage() {
  const [users, setUsers] = useState(mockUsers);
  const [searchTerm, setSearchTerm] = useState('');

  const filteredUsers = users.filter(user =>
    user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <header className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Users</h1>
          <p className="text-gray-500">Manage user accounts and roles</p>
        </div>
        <motion.button 
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          className="flex items-center bg-blue-500 text-white px-4 py-2 rounded-lg shadow-md"
        >
          <UserPlus className="mr-2" size={20} />
          Add User
        </motion.button>
      </header>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="flex items-center mb-4">
          <Search className="text-gray-400 mr-2" />
          <input 
            type="text"
            placeholder="Search by name or email..."
            className="w-full p-2 border border-gray-200 rounded-lg"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <table className="w-full text-left">
          <thead>
            <tr className="border-b bg-gray-50">
              <th className="p-3">Name</th>
              <th className="p-3">Email</th>
              <th className="p-3">Role</th>
              <th className="p-3">Joined Date</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map(user => (
              <motion.tr 
                key={user.id} 
                className="border-b hover:bg-gray-50"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <td className="p-3 font-medium">{user.name}</td>
                <td className="p-3 text-gray-600">{user.email}</td>
                <td className="p-3 text-gray-600">{user.role}</td>
                <td className="p-3 text-gray-600">{user.joined}</td>
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
