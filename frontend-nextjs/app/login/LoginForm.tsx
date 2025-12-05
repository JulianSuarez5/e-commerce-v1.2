'use client';

import Link from 'next/link';
import { useSearchParams } from 'next/navigation';
import { useEffect } from 'react';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';
import { useAuth, LoginCredentials } from '@/hooks/useAuth';
import { AuthForm } from '@/components/AuthForm';

export default function LoginForm() {
  const searchParams = useSearchParams();
  const { login, loading } = useAuth();

  useEffect(() => {
    if (searchParams.get('registered') === 'true') {
      toast.success('¡Registro completado! Por favor, inicia sesión.');
    }
  }, [searchParams]);

  const handleLogin = async (credentials: LoginCredentials) => {
    const toastId = toast.loading('Iniciando sesión...');

    try {
      await login(credentials);
      toast.success('¡Bienvenido de vuelta!', { id: toastId });
    } catch (error: any) {
      toast.error(error.message || 'Credenciales incorrectas. Inténtalo de nuevo.', { id: toastId });
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
          <h1 className="text-4xl font-bold tracking-tight text-gray-900">Accede a tu Cuenta</h1>
          <p className="mt-2 text-gray-600">Bienvenido de nuevo a la vanguardia del e-commerce.</p>
        </div>

        <AuthForm 
          onSubmit={handleLogin} 
          loading={loading} 
          submitText='Iniciar Sesión' 
        />

        <div className="mt-6 text-center text-sm text-gray-600">
          <p>
            ¿Aún no tienes cuenta? {' '}
            <Link href="/register" className="font-semibold text-blue-600 hover:text-blue-700 transition-colors">
              Regístrate aquí
            </Link>
          </p>
        </div>
      </motion.div>
    </div>
  );
}