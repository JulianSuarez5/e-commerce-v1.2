'use client';

import { useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { ArrowLeft, Mail, Lock, User, Phone } from 'lucide-react';
import AppleHeader from '@/components/AppleHeader';
import { Container } from '@/ui/container';
import { Card } from '@/ui/card';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    phone: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: formData.name,
          username: formData.username,
          email: formData.email,
          password: formData.password,
          phone: formData.phone,
        }),
      });

      if (response.ok) {
        window.location.href = '/login?registered=true';
      } else {
        const data = await response.json();
        setError(data.error || 'Error al registrarse');
      }
    } catch (err) {
      setError('Error al registrarse');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        <div className="max-w-md mx-auto">
          <Link 
            href="/" 
            className="inline-flex items-center space-x-2 text-body text-[#007AFF] mb-8 hover:opacity-80 transition-opacity"
          >
            <ArrowLeft className="w-4 h-4" />
            <span>Volver</span>
          </Link>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className=""
          >
            <Card className="p-8">
              <h1 className="text-title text-2xl mb-2">Crear cuenta</h1>
              <p className="text-subtitle mb-8">Únete a nuestra plataforma.</p>

              <form onSubmit={handleSubmit} className="space-y-5">
                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Nombre completo
                  </label>
                  <div className="relative">
                    <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="text"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      className="pl-12"
                      required
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Usuario
                  </label>
                  <Input
                    type="text"
                    value={formData.username}
                    onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Email
                  </label>
                  <div className="relative">
                    <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="email"
                      value={formData.email}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                      className="pl-12"
                      required
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Teléfono
                  </label>
                  <div className="relative">
                    <Phone className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="tel"
                      value={formData.phone}
                      onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                      className="pl-12"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Contraseña
                  </label>
                  <div className="relative">
                    <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="password"
                      value={formData.password}
                      onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                      className="pl-12"
                      required
                      minLength={6}
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Confirmar contraseña
                  </label>
                  <div className="relative">
                    <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="password"
                      value={formData.confirmPassword}
                      onChange={(e) =>
                        setFormData({ ...formData, confirmPassword: e.target.value })
                      }
                      className="pl-12"
                      required
                    />
                  </div>
                </div>

                {error && (
                  <div className="rounded-[10px] border border-[#FFE5E5] bg-[#FFF8F8] px-4 py-3 text-xs text-[#B3261E]">
                    {error}
                  </div>
                )}

                <Button type="submit" disabled={loading} fullWidth>
                  {loading ? 'Creando cuenta…' : 'Crear cuenta'}
                </Button>
              </form>

              <p className="mt-6 text-center text-xs text-[#86868B]">
                ¿Ya tienes cuenta?{' '}
                <Link href="/login" className="text-[#007AFF] hover:opacity-80">
                  Inicia sesión
                </Link>
              </p>
            </Card>
          </motion.div>
        </div>
      </Container>
    </div>
  );
}

