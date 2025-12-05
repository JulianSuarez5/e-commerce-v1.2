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
    <div className="relative flex min-h-screen w-full flex-col items-center justify-center bg-gradient-to-br from-gray-100 via-white to-gray-50 overflow-hidden">
      <div className="absolute inset-0 bg-[url(/grid.svg)] bg-center [mask-image:linear-gradient(180deg,white,rgba(255,255,255,0))]" />

      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, ease: 'easeInOut' }}
        className="relative z-10 w-full max-w-md px-4 py-16"
      >
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold tracking-tight text-gray-900">Crea tu Cuenta</h1>
          <p className="mt-2 text-gray-600">Únete a la nueva era del e-commerce premium.</p>
        </div>

        <div className="relative rounded-2xl border border-gray-200/80 bg-white/60 p-8 shadow-2xl backdrop-blur-lg">
          <form onSubmit={handleRegister} className="space-y-4">
            <div className="relative">
              <User className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
              <Input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Nombre completo"
                className="pl-10"
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
              <Mail className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
              <Input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="tu@email.com"
                className="pl-10"
                required
              />
            </div>
            <div className="relative">
              <Phone className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
              <Input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                placeholder="Teléfono (Opcional)"
                className="pl-10"
              />
            </div>
            <div className="relative">
              <Lock className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
              <Input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Contraseña"
                className="pl-10"
                required
              />
            </div>
            <div className="relative">
              <Lock className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
              <Input
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Confirmar contraseña"
                className="pl-10"
                required
              />
            </div>

            <Button type="submit" disabled={loading} fullWidth size="lg">
              {loading ? 'Creando cuenta...' : 'Crear Cuenta'}
            </Button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            <p>
              ¿Ya tienes una cuenta? {' '}
              <Link href="/login" className="font-semibold text-blue-600 hover:text-blue-700 transition-colors">
                Inicia sesión
              </Link>
            </p>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
