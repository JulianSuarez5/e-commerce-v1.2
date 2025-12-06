'use client';

import * as React from 'react';
import { useState, useEffect, createContext, useContext } from 'react';
import type { ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';

// 1. Definir la forma del contexto
interface AuthContextType {
  user: any; // Se puede definir un tipo más estricto para User
  loading: boolean;
  login: (credentials: any) => Promise<void>;
  register: (userData: any) => Promise<void>;
  logout: () => void;
}

// 2. Crear el Contexto de React
const AuthContext = createContext<AuthContextType | null>(null);

// 3. Crear el componente Proveedor (Provider)
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const auth = useProvideAuth();
  return <AuthContext.Provider value={auth}>{children}</AuthContext.Provider>;
};

// 4. Crear el Hook para usar el contexto fácilmente
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};

// --- Lógica de Autenticación --- 
// Esta función interna maneja toda la lógica y el estado.
const useProvideAuth = () => {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Efecto para verificar el usuario al cargar la página
  useEffect(() => {
    const initializeAuth = async () => {
      const token = localStorage.getItem('accessToken');
      if (token) {
        // No es necesario establecer la cabecera aquí, el interceptor de Axios lo hará
        try {
          // Asumimos que hay un endpoint para obtener el usuario actual
          const { data: userData } = await api.get('/users/me');
          setUser(userData);
        } catch (error) {
          console.error('Token inválido, cerrando sesión:', error);
          localStorage.removeItem('accessToken');
        }
      }
      setLoading(false);
    };
    initializeAuth();
  }, []);

  const login = async (credentials: any) => {
    setLoading(true);
    try {
      const { data } = await api.post('/auth/login', credentials);
      localStorage.setItem('accessToken', data.token);
      // El interceptor de Axios se encargará de añadir el token a las futuras peticiones
      const { data: userData } = await api.get('/users/me');
      setUser(userData);
      router.push('/');
    } catch (error) {
      console.error('Login failed:', error);
      throw error.response?.data || error;
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData: any) => {
    setLoading(true);
    try {
      await api.post('/auth/register', userData);
    } catch (error) {
      console.error('Registration failed:', error);
      throw error.response?.data || error;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    setUser(null);
    router.push('/login');
  };

  return { user, loading, login, register, logout };
};
