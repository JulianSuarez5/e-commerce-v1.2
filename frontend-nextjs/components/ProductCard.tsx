'use client';

import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Button } from '@/ui/button';
import { motion } from 'framer-motion';
import { cn } from '@/lib/cn';
import { ProductDto } from '@/types/product';
import { useAuth } from '@/hooks/useAuth';
import toast from 'react-hot-toast';

interface ProductCardProps {
  product: ProductDto;
  className?: string;
}

export function ProductCard({ product, className }: ProductCardProps) {
  const router = useRouter();
  const { user } = useAuth();
  const imageUrl = product.imageUrl || 'https://placehold.co/600x600/E5E7EB/343A40?text=Producto';

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4, ease: 'easeOut' } },
  };

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (!user) {
      toast.error('Debes iniciar sesión para agregar productos al carrito');
      router.push('/login');
      return;
    }

    try {
      const token = localStorage.getItem('accessToken');
      const response = await fetch('/api/cart/items', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          productId: product.id,
          quantity: 1,
        }),
      });

      if (response.ok) {
        toast.success('Producto agregado al carrito');
      } else {
        const error = await response.json();
        toast.error(error.message || 'Error al agregar al carrito');
      }
    } catch (error) {
      toast.error('Error al agregar al carrito');
    }
  };

  return (
    <Link href={`/products/${product.id}`}>
      <motion.div
        variants={cardVariants}
        initial="hidden"
        animate="visible"
        whileHover={{ y: -5, transition: { duration: 0.2 } }}
        className={cn(
          "group relative flex flex-col overflow-hidden rounded-[16px] border border-[#E5E5E7] bg-white shadow-sm transition-all duration-300 hover:shadow-lg",
          className
        )}
      >
        <div className="relative aspect-square w-full overflow-hidden bg-[#F5F5F7]">
          <Image
            src={imageUrl.startsWith('http') ? imageUrl : `http://localhost:8081${imageUrl}`}
            alt={product.name}
            fill
            className="object-cover transition-transform duration-300 group-hover:scale-105"
            sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
          />
        </div>

        <div className="flex flex-1 flex-col p-4">
          <div className="mb-2">
            <p className="text-xs text-[#86868B] mb-1">{product.categoryName || 'Sin categoría'}</p>
            <h3 className="text-title text-base text-[#1D1D1F] line-clamp-2">{product.name}</h3>
          </div>
          <div className="mt-auto">
            <p className="text-title text-lg text-[#1D1D1F] mb-3">${product.price.toFixed(2)}</p>
            <Button 
              onClick={handleAddToCart}
              className="w-full"
              size="md"
            >
              Agregar al Carrito
            </Button>
          </div>
        </div>
      </motion.div>
    </Link>
  );
}
