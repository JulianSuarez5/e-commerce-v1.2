'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';

export const useAuth = () => {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(false);

  const login = async (credentials) => {
    setLoading(true);
    try {
      const { data } = await api.post('/auth/login', credentials);
      localStorage.setItem('accessToken', data.token);
      api.defaults.headers.Authorization = `Bearer ${data.token}`;
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

  const register = async (userData) => {
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
    delete api.defaults.headers.Authorization;
    setUser(null);
    router.push('/login');
  };

  return { user, loading, login, register, logout };
};
