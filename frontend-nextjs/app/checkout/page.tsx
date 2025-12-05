'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { ArrowLeft, MapPin, CreditCard } from 'lucide-react';
import AppleHeader from '@/components/AppleHeader';
import { CartDto } from '@/types/cart';
import { Container } from '@/ui/container';
import { Card } from '@/ui/card';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';

export default function CheckoutPage() {
  const router = useRouter();
  const [cart, setCart] = useState<CartDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [formData, setFormData] = useState({
    shippingAddress: '',
    shippingCity: '',
    shippingState: '',
    shippingZipCode: '',
    paymentMethod: 'PAYPAL',
    notes: '',
  });

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        router.push('/login');
        return;
      }

      const response = await fetch('/api/cart', {
        headers: { 'Authorization': `Bearer ${token}` },
      });

      if (response.ok) {
        const data = await response.json();
        setCart(data);
        if (data.items.length === 0) {
          router.push('/cart');
        }
      }
    } catch (error) {
      console.error('Error fetching cart:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setProcessing(true);

    try {
      const token = localStorage.getItem('accessToken');
      const response = await fetch('/api/checkout', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const order = await response.json();
        router.push(`/orders/${order.id}?success=true`);
      } else {
        alert('Error al procesar el checkout');
      }
    } catch (error) {
      alert('Error al procesar el checkout');
    } finally {
      setProcessing(false);
    }
  };

  if (loading || !cart) {
    return (
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />
        <div className="flex items-center justify-center min-h-[60vh]">
          <div className="w-12 h-12 border-4 border-[#E5E5E7] border-t-[#007AFF] rounded-full animate-spin" />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        <Link 
          href="/cart" 
          className="inline-flex items-center space-x-2 text-body text-[#007AFF] mb-8 hover:opacity-80 transition-opacity"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Volver al carrito</span>
        </Link>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Form */}
          <div className="lg:col-span-2">
            <motion.form
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              onSubmit={handleSubmit}
              className="space-y-6"
            >
              {/* Shipping Address */}
              <Card className="p-6">
                <div className="flex items-center space-x-2 mb-6">
                  <MapPin className="w-5 h-5 text-[#007AFF]" />
                  <h2 className="text-title text-lg text-[#1D1D1F]">
                    Dirección de Envío
                  </h2>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-xs font-medium text-[#86868B] mb-2">
                      Dirección
                    </label>
                    <Input
                      type="text"
                      value={formData.shippingAddress}
                      onChange={(e) => setFormData({ ...formData, shippingAddress: e.target.value })}
                      required
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-medium text-[#86868B] mb-2">
                        Ciudad
                      </label>
                      <Input
                        type="text"
                        value={formData.shippingCity}
                        onChange={(e) => setFormData({ ...formData, shippingCity: e.target.value })}
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-[#86868B] mb-2">
                        Estado
                      </label>
                      <Input
                        type="text"
                        value={formData.shippingState}
                        onChange={(e) => setFormData({ ...formData, shippingState: e.target.value })}
                        required
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-medium text-[#86868B] mb-2">
                      Código Postal
                    </label>
                    <Input
                      type="text"
                      value={formData.shippingZipCode}
                      onChange={(e) => setFormData({ ...formData, shippingZipCode: e.target.value })}
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-medium text-[#86868B] mb-2">
                      Notas (opcional)
                    </label>
                    <textarea
                      value={formData.notes}
                      onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                      className="w-full rounded-[10px] border border-[#E5E5E7] bg-[#F5F5F7] px-4 py-3 text-sm text-[#1D1D1F] outline-none transition-all duration-200 ease-[0.4,0,0.2,1] focus:border-[#007AFF] focus:ring-4 focus:ring-[rgba(0,122,255,0.12)] min-h-[100px] resize-none"
                      placeholder="Instrucciones especiales de entrega..."
                    />
                  </div>
                </div>
              </Card>

              {/* Payment Method */}
              <Card className="p-6">
                <div className="flex items-center space-x-2 mb-6">
                  <CreditCard className="w-5 h-5 text-[#007AFF]" />
                  <h2 className="text-title text-lg text-[#1D1D1F]">
                    Método de Pago
                  </h2>
                </div>

                <div className="space-y-3">
                  {['PAYPAL', 'CREDIT_CARD'].map((method) => (
                    <label
                      key={method}
                      className={`flex items-center space-x-3 p-4 rounded-[12px] border-2 cursor-pointer transition-colors ${
                        formData.paymentMethod === method
                          ? 'border-[#007AFF] bg-[#F5F5F7]'
                          : 'border-[#E5E5E7] hover:border-[#D2D2D7]'
                      }`}
                    >
                      <input
                        type="radio"
                        name="paymentMethod"
                        value={method}
                        checked={formData.paymentMethod === method}
                        onChange={(e) => setFormData({ ...formData, paymentMethod: e.target.value })}
                        className="w-5 h-5 text-[#007AFF]"
                      />
                      <span className="text-body text-[#1D1D1F]">
                        {method === 'PAYPAL' ? 'PayPal' : 'Tarjeta de Crédito'}
                      </span>
                    </label>
                  ))}
                </div>
              </Card>

              <Button type="submit" disabled={processing} fullWidth>
                {processing ? 'Procesando…' : 'Completar compra'}
              </Button>
            </motion.form>
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1">
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              className=""
            >
              <Card className="p-6 sticky top-24">
                <h2 className="text-title text-lg text-[#1D1D1F] mb-6">
                  Resumen del pedido
                </h2>

                <div className="space-y-4 mb-6">
                  {cart.items.map((item) => (
                    <div key={item.id} className="flex justify-between text-body">
                      <span className="text-[#86868B]">
                        {item.productName} x{item.quantity}
                      </span>
                      <span className="text-[#1D1D1F]">
                        ${item.subtotal.toFixed(2)}
                      </span>
                    </div>
                  ))}
                </div>

                <div className="border-t border-[#E5E5E7] pt-4 space-y-2">
                  <div className="flex justify-between text-body text-[#86868B]">
                    <span>Subtotal</span>
                    <span>${cart.totalPrice.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between text-body text-[#86868B]">
                    <span>Envío</span>
                    <span className="text-[#007AFF]">Gratis</span>
                  </div>
                  <div className="flex justify-between pt-2 border-t border-[#E5E5E7]">
                    <span className="text-body font-medium text-[#1D1D1F]">
                      Total
                    </span>
                    <span className="text-title text-lg text-[#1D1D1F]">
                      ${cart.totalPrice.toFixed(2)}
                    </span>
                  </div>
                </div>
              </Card>
            </motion.div>
          </div>
        </div>
      </Container>
    </div>
  );
}

