'use client';

import { useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { ArrowLeft, Mail, Lock } from 'lucide-react';
import AppleHeader from '@/components/AppleHeader';
import { Container } from '@/ui/container';
import { Card } from '@/ui/card';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        window.location.href = '/products';
      } else {
        setError('Credenciales inválidas');
      }
    } catch (err) {
      setError('Error al iniciar sesión');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        <div className="max-w-md mx-auto">
          {/* Back Button */}
          <Link 
            href="/" 
            className="inline-flex items-center space-x-2 text-body text-[#007AFF] mb-8 hover:opacity-80 transition-opacity"
          >
            <ArrowLeft className="w-4 h-4" />
            <span>Volver</span>
          </Link>

          {/* Login Card */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className=""
          >
            <Card className="p-8">
              <h1 className="text-title text-2xl mb-2">Iniciar sesión</h1>
              <p className="text-subtitle mb-8">
                Ingresa a tu cuenta para continuar.
              </p>

              <form onSubmit={handleSubmit} className="space-y-6">
                {/* Email */}
                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Email o usuario
                  </label>
                  <div className="relative">
                    <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="text"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      placeholder="tu@email.com"
                      className="pl-12"
                      required
                    />
                  </div>
                </div>

                {/* Password */}
                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Contraseña
                  </label>
                  <div className="relative">
                    <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-[#86868B]" />
                    <Input
                      type="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="••••••••"
                      className="pl-12"
                      required
                    />
                  </div>
                </div>

                {/* Error */}
                {error && (
                  <div className="rounded-[10px] border border-[#FFE5E5] bg-[#FFF8F8] px-4 py-3 text-xs text-[#B3261E]">
                    {error}
                  </div>
                )}

                {/* Submit */}
                <Button type="submit" disabled={loading} fullWidth>
                  {loading ? 'Iniciando sesión…' : 'Iniciar sesión'}
                </Button>
              </form>

              {/* Links */}
              <div className="mt-6 space-y-3 text-center">
                <Link
                  href="/forgot-password"
                  className="block text-body text-[#007AFF] hover:opacity-80"
                >
                  ¿Olvidaste tu contraseña?
                </Link>
                <p className="text-xs text-[#86868B]">
                  ¿No tienes cuenta?{' '}
                  <Link href="/register" className="text-[#007AFF] hover:opacity-80">
                    Regístrate
                  </Link>
                </p>
              </div>
            </Card>
          </motion.div>
        </div>
      </Container>
    </div>
  );
}

