'use client';

import Image from 'next/image';
import { Button } from '@/ui/button';
import { motion } from 'framer-motion';
import { cn } from '@/lib/cn';
import { ProductDto } from '@/types/product';

interface ProductCardProps {
  product: ProductDto;
  className?: string;
}

export function ProductCard({ product, className }: ProductCardProps) {
  const imageUrl = product.imageUrl || 'https://placehold.co/600x600/E5E7EB/343A40?text=Producto';

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.4, ease: 'easeOut' } },
  };

  return (
    <motion.div
      variants={cardVariants}
      initial="hidden"
      animate="visible"
      whileHover={{ y: -5, transition: { duration: 0.2 } }}
      className={cn(
        "group relative flex flex-col overflow-hidden rounded-2xl border border-gray-200/80 bg-white/50 backdrop-blur-xl shadow-lg transition-all duration-300 hover:shadow-2xl",
        className
      )}
    >
      <div className="relative aspect-square w-full overflow-hidden">
        <Image
          src={imageUrl}
          alt={product.name}
          fill
          className="object-cover transition-transform duration-300 group-hover:scale-105"
          sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
        />
        <div className="absolute inset-x-0 bottom-0 h-1/3 bg-gradient-to-t from-black/60 to-transparent" />
        <div className="absolute inset-x-0 bottom-0 p-4">
          <p className="text-sm font-medium text-white/80">{product.categoryName}</p>
          <h3 className="mt-1 text-lg font-semibold text-white">{product.name}</h3>
        </div>
      </div>

      <div className="flex flex-1 flex-col p-4">
        <div className="flex-1">
          <p className="text-lg font-semibold text-gray-900">${product.price.toFixed(2)}</p>
        </div>
        <Button className="mt-4 w-full rounded-lg bg-blue-600 text-white transition-colors hover:bg-blue-700">
          Agregar al Carrito
        </Button>
      </div>
    </motion.div>
  );
}
