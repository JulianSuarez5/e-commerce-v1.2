'use client';

import { ReactNode } from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  LayoutDashboard, 
  ShoppingCart, 
  Users, 
  Settings, 
  Boxes, 
  Building2 
} from 'lucide-react';

const sidebarVariants = {
  hidden: { x: -250 },
  visible: { x: 0 },
};

const mainVariants = {
  expanded: { marginLeft: 250 },
  collapsed: { marginLeft: 0 },
};

const navItems = [
  { href: '/admin/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { href: '/admin/products', label: 'Products', icon: Boxes },
  { href: '/admin/orders', label: 'Orders', icon: ShoppingCart },
  { href: '/admin/users', label: 'Users', icon: Users },
  { href: '/admin/sellers', label: 'Sellers', icon: Building2 },
  { href: '/admin/settings', label: 'Settings', icon: Settings },
];

export default function AdminLayout({ children }: { children: ReactNode }) {
  const pathname = usePathname();

  return (
    <div className="flex h-screen bg-gray-100">
      <motion.aside
        variants={sidebarVariants}
        initial="visible"
        animate="visible"
        transition={{ duration: 0.3 }}
        className="w-64 bg-white shadow-md flex-shrink-0"
      >
        <div className="p-4 border-b">
          <h2 className="text-2xl font-bold text-gray-800">Admin Panel</h2>
        </div>
        <nav className="p-4">
          <ul>
            {navItems.map(({ href, label, icon: Icon }) => (
              <li key={href}>
                <Link
                  href={href}
                  className={`flex items-center p-3 my-1 rounded-lg transition-colors ${
                    pathname === href
                      ? 'bg-blue-500 text-white'
                      : 'text-gray-600 hover:bg-gray-200'
                  }`}
                >
                  <Icon className="mr-3" size={20} />
                  {label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </motion.aside>

      <motion.main
        variants={mainVariants}
        initial="expanded"
        animate="expanded"
        transition={{ duration: 0.3 }}
        className="flex-1 overflow-y-auto"
      >
        <AnimatePresence mode="wait">
          <motion.div
            key={pathname}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ duration: 0.3 }}
          >
            {children}
          </motion.div>
        </AnimatePresence>
      </motion.main>
    </div>
  );
}
