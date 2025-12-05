'use client';

import Link from 'next/link';
import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Search, ShoppingCart, User, Menu, X } from 'lucide-react';
import { Container } from '@/ui/container';

export default function AppleHeader() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  return (
    <header className="fixed top-0 left-0 right-0 z-50 border-b border-[#E5E5E7] bg-white/90 backdrop-blur-xl">
      <Container className="h-16 flex items-center justify-between">
        <div className="flex items-center justify-between w-full">
          {/* Logo */}
          <Link href="/" className="flex items-center space-x-2">
            <span className="text-title text-base text-[#1D1D1F]">E‑Commerce</span>
            <span className="text-body text-sm text-[#86868B]">Pro</span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center space-x-8">
            <Link
              href="/products"
              className="text-body text-sm text-[#1D1D1F] hover:text-[#007AFF] transition-colors"
            >
              Productos
            </Link>
            <Link
              href="/categories"
              className="text-body text-sm text-[#1D1D1F] hover:text-[#007AFF] transition-colors"
            >
              Categorías
            </Link>
            <Link
              href="/sellers"
              className="text-body text-sm text-[#1D1D1F] hover:text-[#007AFF] transition-colors"
            >
              Vendedores
            </Link>
          </nav>

          {/* Actions */}
          <div className="flex items-center space-x-4">
            {/* Search */}
            <button className="p-2 rounded-full hover:bg-[#F5F5F7] transition-colors">
              <Search className="w-5 h-5 text-[#1D1D1F]" />
            </button>

            {/* Cart */}
            <Link 
              href="/cart" 
              className="relative p-2 rounded-full hover:bg-[#F5F5F7] transition-colors"
            >
              <ShoppingCart className="w-5 h-5 text-[#1D1D1F]" />
              <span className="absolute top-0 right-0 w-4 h-4 bg-[#007AFF] text-white text-[10px] rounded-full flex items-center justify-center">
                0
              </span>
            </Link>

            {/* User */}
            <Link 
              href="/login" 
              className="p-2 rounded-full hover:bg-[#F5F5F7] transition-colors"
            >
              <User className="w-5 h-5 text-[#1D1D1F]" />
            </Link>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="md:hidden p-2 rounded-full hover:bg-[#F5F5F7] transition-colors"
            >
              {mobileMenuOpen ? (
                <X className="w-5 h-5 text-[#1D1D1F]" />
              ) : (
                <Menu className="w-5 h-5 text-[#1D1D1F]" />
              )}
            </button>
          </div>
        </div>
      </Container>

      {/* Mobile Menu */}
      <AnimatePresence>
        {mobileMenuOpen && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            className="md:hidden border-t border-[#E5E5E7] bg-white/95 backdrop-blur-xl"
          >
            <Container className="py-4 space-y-4">
              <Link
                href="/products"
                className="block text-body text-sm text-[#1D1D1F] py-2"
                onClick={() => setMobileMenuOpen(false)}
              >
                Productos
              </Link>
              <Link
                href="/categories"
                className="block text-body text-sm text-[#1D1D1F] py-2"
                onClick={() => setMobileMenuOpen(false)}
              >
                Categorías
              </Link>
              <Link
                href="/sellers"
                className="block text-body text-sm text-[#1D1D1F] py-2"
                onClick={() => setMobileMenuOpen(false)}
              >
                Vendedores
              </Link>
            </Container>
          </motion.div>
        )}
      </AnimatePresence>
    </header>
  );
}

