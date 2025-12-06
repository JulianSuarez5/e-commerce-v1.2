'use client';

import axios from 'axios';

// Obtiene la URL base de la API desde las variables de entorno
const baseURL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081';

// Crea una instancia de Axios
export const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Opcional: Interceptor para añadir el token de autenticación a todas las peticiones
api.interceptors.request.use(
  (config) => {
    // Solo se ejecuta en el lado del cliente
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
