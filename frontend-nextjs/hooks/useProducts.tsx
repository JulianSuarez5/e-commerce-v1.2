'use client';

import { useState, useEffect } from 'react';
import { api } from '@/lib/api';
import { ProductDto } from '@/types/product';

const sampleProducts: ProductDto[] = [
  {
    id: 1,
    name: 'iPhone 15 Pro',
    price: 999,
    imageUrl: '/placeholder-image.png',
    categoryName: 'Smartphones',
    description: 'The latest iPhone.',
    cantidad: 100,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    categoryId: 1,
    brandId: 1,
    brandName: 'Apple',
    sellerId: 1,
    sellerName: 'Apple Store',
    model3dUrl: null,
  },
  {
    id: 2,
    name: 'MacBook Air M3',
    price: 1299,
    imageUrl: '/placeholder-image.png',
    categoryName: 'Laptops',
    description: 'The latest MacBook.',
    cantidad: 50,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    categoryId: 2,
    brandId: 1,
    brandName: 'Apple',
    sellerId: 1,
    sellerName: 'Apple Store',
    model3dUrl: null,
  },
  {
    id: 3,
    name: 'Sony WH-1000XM5',
    price: 399,
    imageUrl: '/placeholder-image.png',
    categoryName: 'Audio',
    description: 'Noise-cancelling headphones.',
    cantidad: 200,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    categoryId: 3,
    brandId: 2,
    brandName: 'Sony',
    sellerId: 2,
    sellerName: 'Sony Store',
    model3dUrl: null,
  },
  {
    id: 4,
    name: 'Apple Watch Series 9',
    price: 499,
    imageUrl: '/placeholder-image.png',
    categoryName: 'Wearables',
    description: 'The latest Apple Watch.',
    cantidad: 150,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    categoryId: 4,
    brandId: 1,
    brandName: 'Apple',
    sellerId: 1,
    sellerName: 'Apple Store',
    model3dUrl: null,
  },
];

export const useProducts = () => {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        // Añadir tipo explícito a la respuesta de la API para mayor seguridad
        const response = await api.get<ProductDto[]>('/products');
        setProducts(response.data);
      } catch (error) {
        console.error('Error fetching products from API, using sample data:', error);
        setProducts(sampleProducts);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  return { products, loading };
};
