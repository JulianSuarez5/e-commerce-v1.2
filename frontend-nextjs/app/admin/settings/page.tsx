'use client';

import { useState, ChangeEvent } from 'react';
import { motion } from 'framer-motion';
import { Save } from 'lucide-react';

export default function SettingsPage() {
  const [settings, setSettings] = useState({
    siteName: 'E-Commerce Store',
    maintenanceMode: false,
    shippingRate: 5.00,
    taxRate: 0.08,
  });

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setSettings(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Settings</h1>
        <p className="text-gray-500">Configure global site settings</p>
      </header>

      <motion.div 
        className="bg-white p-6 rounded-lg shadow-md max-w-2xl"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <form>
          <div className="space-y-6">
            <div>
              <label htmlFor="siteName" className="block text-sm font-medium text-gray-700">Site Name</label>
              <input 
                type="text"
                id="siteName"
                name="siteName"
                value={settings.siteName}
                onChange={handleInputChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm"
              />
            </div>

            <div>
              <label htmlFor="shippingRate" className="block text-sm font-medium text-gray-700">Standard Shipping Rate ($)</label>
              <input 
                type="number"
                id="shippingRate"
                name="shippingRate"
                value={settings.shippingRate}
                onChange={handleInputChange}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm"
              />
            </div>

            <div>
              <label htmlFor="taxRate" className="block text-sm font-medium text-gray-700">Tax Rate (%)</label>
              <input 
                type="number"
                id="taxRate"
                name="taxRate"
                value={settings.taxRate * 100}
                onChange={(e: ChangeEvent<HTMLInputElement>) => setSettings(prev => ({...prev, taxRate: parseFloat(e.target.value) / 100}))}
                className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm"
              />
            </div>

            <div className="flex items-center">
              <input 
                id="maintenanceMode"
                name="maintenanceMode"
                type="checkbox"
                checked={settings.maintenanceMode}
                onChange={handleInputChange}
                className="h-4 w-4 text-blue-600 border-gray-300 rounded"
              />
              <label htmlFor="maintenanceMode" className="ml-2 block text-sm text-gray-900">Enable Maintenance Mode</label>
            </div>
          </div>

          <div className="mt-8">
            <motion.button 
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              type="submit"
              className="flex items-center bg-blue-500 text-white px-4 py-2 rounded-lg shadow-md"
            >
              <Save className="mr-2" size={20} />
              Save Settings
            </motion.button>
          </div>
        </form>
      </motion.div>
    </div>
  );
}
