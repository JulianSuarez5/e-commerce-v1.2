'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';

// Definición del tipo de usuario para mejorar la seguridad de tipos
export interface User {
  id: string;
  name: string;
  email: string;
  role: string;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials extends LoginCredentials {
  name: string;
}

export const useAuth = () => {
  const router = useRouter();
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);

  const login = async (credentials: LoginCredentials) => {
    setLoading(true);
    try {
      const { data } = await api.post('/auth/login', credentials);
      localStorage.setItem('accessToken', data.token);
      // Práctica recomendada para establecer cabeceras de autorización con Axios
      api.defaults.headers.common['Authorization'] = `Bearer ${data.token}`;
      const { data: userData } = await api.get<User>('/users/me');
      setUser(userData);
      router.push('/');
    } catch (error: any) {
      console.error('Login failed:', error);
      throw error.response?.data || error;
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData: RegisterCredentials) => {
    setLoading(true);
    try {
      await api.post('/auth/register', userData);
    } catch (error: any) {
      console.error('Registration failed:', error);
      throw error.response?.data || error;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    // Eliminar la cabecera de autorización de las futuras solicitudes
    delete api.defaults.headers.common['Authorization'];
    setUser(null);
    router.push('/login');
  };

  return { user, loading, login, register, logout };
};
