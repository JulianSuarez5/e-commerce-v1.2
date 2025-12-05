'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { ShoppingCart, Trash2, Plus, Minus, ArrowRight } from 'lucide-react';
import AppleHeader from '@/components/AppleHeader';
import { CartDto } from '@/types/cart';
import { Container } from '@/ui/container';
import { Card } from '@/ui/card';
import { Button } from '@/ui/button';

export default function CartPage() {
  const [cart, setCart] = useState<CartDto | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        window.location.href = '/login';
        return;
      }

      const response = await fetch('/api/cart', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setCart(data);
      }
    } catch (error) {
      console.error('Error fetching cart:', error);
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = async (itemId: number, quantity: number) => {
    try {
      const token = localStorage.getItem('accessToken');
      const response = await fetch(`/api/cart/items/${itemId}?quantity=${quantity}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        fetchCart();
      }
    } catch (error) {
      console.error('Error updating quantity:', error);
    }
  };

  const removeItem = async (itemId: number) => {
    try {
      const token = localStorage.getItem('accessToken');
      const response = await fetch(`/api/cart/items/${itemId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        fetchCart();
      }
    } catch (error) {
      console.error('Error removing item:', error);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />
        <div className="flex items-center justify-center min-h-[60vh]">
          <div className="w-12 h-12 border-4 border-[#E5E5E7] border-t-[#007AFF] rounded-full animate-spin" />
        </div>
      </div>
    );
  }

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />
        <Container className="pt-24 pb-16">
          <div className="text-center py-16">
            <ShoppingCart className="w-16 h-16 text-[#86868B] mx-auto mb-4" />
            <h2 className="text-title text-2xl text-[#1D1D1F] mb-2">
              Tu carrito está vacío
            </h2>
            <p className="text-body text-[#86868B] mb-8">
              Agrega productos para comenzar
            </p>
            <Button asChild>
              <Link href="/products">Explorar productos</Link>
            </Button>
          </div>
        </Container>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        <h1 className="text-display mb-8">
          Carrito de Compras
        </h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Items */}
          <div className="lg:col-span-2 space-y-4">
            {cart.items.map((item) => (
              <motion.div
                key={item.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className=""
              >
                <Card className="p-6">
                  <div className="flex space-x-4">
                  {/* Image */}
                  <div className="w-24 h-24 bg-[#F5F5F7] rounded-[12px] overflow-hidden flex-shrink-0">
                    {item.productImageUrl ? (
                      <img
                        src={`http://localhost:8081${item.productImageUrl}`}
                        alt={item.productName}
                        className="w-full h-full object-cover"
                      />
                    ) : (
                      <div className="w-full h-full flex items-center justify-center">
                        <ShoppingCart className="w-8 h-8 text-[#86868B]" />
                      </div>
                    )}
                  </div>

                  {/* Info */}
                  <div className="flex-grow">
                    <h3 className="text-title text-base text-[#1D1D1F] mb-1">
                      {item.productName}
                    </h3>
                    <p className="text-body text-[#86868B] mb-3">
                      ${item.price.toFixed(2)} c/u
                    </p>

                    {/* Quantity Controls */}
                    <div className="flex items-center space-x-3">
                      <button
                        onClick={() => updateQuantity(item.id, item.quantity - 1)}
                        className="w-8 h-8 rounded-full border border-[#E5E5E7] flex items-center justify-center hover:bg-[#F5F5F7] transition-colors"
                      >
                        <Minus className="w-4 h-4 text-[#1D1D1F]" />
                      </button>
                      <span className="text-body text-[#1D1D1F] w-8 text-center">
                        {item.quantity}
                      </span>
                      <button
                        onClick={() => updateQuantity(item.id, item.quantity + 1)}
                        className="w-8 h-8 rounded-full border border-[#E5E5E7] flex items-center justify-center hover:bg-[#F5F5F7] transition-colors"
                      >
                        <Plus className="w-4 h-4 text-[#1D1D1F]" />
                      </button>
                    </div>
                  </div>

                  {/* Price and Remove */}
                  <div className="flex flex-col items-end justify-between">
                    <button
                      onClick={() => removeItem(item.id)}
                      className="p-2 rounded-full hover:bg-[#F5F5F7] transition-colors"
                    >
                      <Trash2 className="w-5 h-5 text-[#86868B]" />
                    </button>
                    <span className="text-title text-lg text-[#1D1D1F]">
                      ${item.subtotal.toFixed(2)}
                    </span>
                  </div>
                  </div>
                </Card>
              </motion.div>
            ))}
          </div>

          {/* Summary */}
          <div className="lg:col-span-1">
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              className=""
            >
              <Card className="p-6 sticky top-24">
                <h2 className="text-title text-lg text-[#1D1D1F] mb-6">
                Resumen
              </h2>

              <div className="space-y-4 mb-6">
                <div className="flex justify-between text-body text-[#86868B]">
                  <span>Subtotal</span>
                  <span>${cart.totalPrice.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-body text-[#86868B]">
                  <span>Envío</span>
                  <span className="text-[#007AFF]">Gratis</span>
                </div>
                <div className="border-t border-[#E5E5E7] pt-4">
                  <div className="flex justify-between">
                    <span className="text-body font-medium text-[#1D1D1F]">
                      Total
                    </span>
                    <span className="text-title text-lg text-[#1D1D1F]">
                      ${cart.totalPrice.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              <Button asChild fullWidth className="flex items-center justify-center space-x-2">
                <Link href="/checkout">
                  <span>Continuar al checkout</span>
                  <ArrowRight className="w-4 h-4 ml-2" />
                </Link>
              </Button>
              </Card>
            </motion.div>
          </div>
        </div>
      </Container>
    </div>
  );
}

