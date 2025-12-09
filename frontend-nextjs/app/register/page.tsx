'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import { useAuth } from '@/hooks/useAuth';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';
import { Mail, Lock, User, Phone } from 'lucide-react';

export default function RegisterPage() {
  const router = useRouter();
  const { register, loading } = useAuth();
  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      toast.error('Las contraseñas no coinciden.');
      return;
    }

    const toastId = toast.loading('Creando tu cuenta...');

    try {
      const { password, confirmPassword, ...registerData } = formData;
      await register({ ...registerData, password });
      toast.success('¡Registro completado! Por favor, inicia sesión.', { id: toastId });
      router.push('/login');
    } catch (error: any) {
      toast.error(error.message || 'Error al registrar la cuenta.', { id: toastId });
    }
  };

  return (
    <div className="relative flex min-h-screen w-full flex-col items-center justify-center bg-white overflow-hidden">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, ease: 'easeInOut' }}
        className="relative z-10 w-full max-w-md px-4 py-16"
      >
        <div className="text-center mb-8">
          <h1 className="text-display text-[#1D1D1F] mb-2">Crea tu Cuenta</h1>
          <p className="text-subtitle">Únete a la nueva era del e-commerce premium.</p>
        </div>

        <div className="relative rounded-[16px] border border-[#E5E5E7] bg-white p-8 shadow-sm">
          <form onSubmit={handleRegister} className="space-y-4">
            <div className="relative">
              <User className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
              <Input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Nombre completo"
                className="pl-12"
                required
              />
            </div>
            <div className="relative">
              <Input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Nombre de usuario"
                required
              />
            </div>
            <div className="relative">
              <Mail className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
              <Input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="tu@email.com"
                className="pl-12"
                required
              />
            </div>
            <div className="relative">
              <Phone className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
              <Input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                placeholder="Teléfono (Opcional)"
                className="pl-12"
              />
            </div>
            <div className="relative">
              <Lock className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
              <Input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Contraseña"
                className="pl-12"
                required
              />
            </div>
            <div className="relative">
              <Lock className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
              <Input
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Confirmar contraseña"
                className="pl-12"
                required
              />
            </div>

            <Button type="submit" disabled={loading} fullWidth size="lg">
              {loading ? 'Creando cuenta...' : 'Crear Cuenta'}
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-[#86868B]">
            <p>
              ¿Ya tienes una cuenta? {' '}
              <Link href="/login" className="font-medium text-[#007AFF] hover:text-[#0A84FF] transition-colors">
                Inicia sesión
              </Link>
            </p>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
