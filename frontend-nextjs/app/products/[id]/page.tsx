'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { ArrowLeft, ShoppingCart, Heart, Share2 } from 'lucide-react';
import AppleHeader from '@/components/AppleHeader';
import Product3DViewer from '@/components/Product3DViewer';
import { ProductDto } from '@/types/product';
import { Container } from '@/ui/container';
import { Button } from '@/ui/button';

export default function ProductDetailPage() {
  const params = useParams();
  const [product, setProduct] = useState<ProductDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  useEffect(() => {
    if (params.id) {
      fetchProduct(Number(params.id));
    }
  }, [params.id]);

  const fetchProduct = async (id: number) => {
    try {
      const response = await fetch(`/api/products/${id}`);
      if (response.ok) {
        const data = await response.json();
        setProduct(data);
      }
    } catch (error) {
      console.error('Error fetching product:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />
        <div className="flex items-center justify-center min-h-[60vh]">
          <div className="text-center">
            <div className="w-12 h-12 border-4 border-[#E5E5E7] border-t-[#007AFF] rounded-full animate-spin mx-auto mb-4" />
            <p className="text-body text-[#86868B]">Cargando producto...</p>
          </div>
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="min-h-screen bg-[#FFFFFF]">
        <AppleHeader />
        <div className="flex items-center justify-center min-h-[60vh]">
          <div className="text-center">
            <p className="text-title text-lg text-[#1D1D1F] mb-2">Producto no encontrado</p>
            <Link href="/products" className="text-body text-[#007AFF]">
              Volver a productos
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const images = product.images || [];
  const primaryImage = images.find(img => img.isPrimary) || images[0] || { url: product.imageUrl };

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        {/* Back Button */}
        <Link
          href="/products"
          className="inline-flex items-center space-x-2 text-body text-[#007AFF] mb-8 hover:opacity-80 transition-opacity"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Volver a productos</span>
        </Link>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Left: 3D Viewer / Images */}
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5 }}
          >
            {product.model3dUrl ? (
              <Product3DViewer product={product} />
            ) : (
              <div className="rounded-[12px] border border-[#E5E5E7] bg-white shadow-sm aspect-square overflow-hidden">
                <img
                  src={`http://localhost:8081${primaryImage.url || product.imageUrl}`}
                  alt={product.name}
                  className="w-full h-full object-cover"
                />
              </div>
            )}

            {/* Image Gallery */}
            {images.length > 1 && (
              <div className="grid grid-cols-4 gap-3 mt-4">
                {images.map((img, index) => (
                  <button
                    key={img.id}
                    onClick={() => setSelectedImageIndex(index)}
                    className={`rounded-[12px] border border-[#E5E5E7] bg-white shadow-sm aspect-square overflow-hidden ${
                      selectedImageIndex === index ? 'ring-2 ring-[#007AFF]' : ''
                    }`}
                  >
                    <img
                      src={`http://localhost:8081${img.thumbnailUrl || img.url}`}
                      alt={`${product.name} ${index + 1}`}
                      className="w-full h-full object-cover"
                    />
                  </button>
                ))}
              </div>
            )}
          </motion.div>

          {/* Right: Product Info */}
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.1 }}
            className="space-y-6"
          >
            {/* Title */}
            <div>
              <h1 className="text-display mb-2">
                {product.name}
              </h1>
              {product.sellerName && (
                <p className="text-body text-[#86868B]">
                  Vendido por <span className="text-[#007AFF]">{product.sellerName}</span>
                </p>
              )}
            </div>

            {/* Price */}
            <div className="py-6 border-y border-[#E5E5E7]">
              <div className="flex items-baseline space-x-2">
                <span className="text-display text-[#1D1D1F] text-3xl">
                  ${product.price.toFixed(2)}
                </span>
                <span className="text-body text-[#86868B]">USD</span>
              </div>
            </div>

            {/* Description */}
            {product.description && (
              <div>
                <h2 className="text-title text-lg text-[#1D1D1F] mb-3">
                  Descripción
                </h2>
                <p className="text-body text-[#1D1D1F] leading-relaxed">
                  {product.description}
                </p>
              </div>
            )}

            {/* Details */}
            <div className="space-y-3">
              {product.categoryName && (
                <div className="flex items-center justify-between py-2 border-b border-[#E5E5E7]">
                <span className="text-body text-[#86868B]">Categoría</span>
                  <span className="text-body text-[#1D1D1F]">{product.categoryName}</span>
                </div>
              )}
              {product.brandName && (
                <div className="flex items-center justify-between py-2 border-b border-[#E5E5E7]">
                <span className="text-body text-[#86868B]">Marca</span>
                  <span className="text-body text-[#1D1D1F]">{product.brandName}</span>
                </div>
              )}
              <div className="flex items-center justify-between py-2 border-b border-[#E5E5E7]">
                <span className="text-body text-[#86868B]">Stock disponible</span>
                <span className="text-body text-[#1D1D1F] font-medium">
                  {product.cantidad} unidades
                </span>
              </div>
            </div>

            {/* Actions */}
            <div className="space-y-3 pt-6">
              <Button fullWidth className="flex items-center justify-center space-x-2">
                <ShoppingCart className="w-5 h-5" />
                <span>Agregar al carrito</span>
              </Button>
              
              <div className="flex space-x-3">
                <Button
                  variant="secondary"
                  className="flex-1 flex items-center justify-center space-x-2"
                >
                  <Heart className="w-5 h-5" />
                  <span>Guardar</span>
                </Button>
                <Button
                  variant="secondary"
                  className="flex-1 flex items-center justify-center space-x-2"
                >
                  <Share2 className="w-5 h-5" />
                  <span>Compartir</span>
                </Button>
              </div>
            </div>
          </motion.div>
        </div>
      </Container>
    </div>
  );
}
