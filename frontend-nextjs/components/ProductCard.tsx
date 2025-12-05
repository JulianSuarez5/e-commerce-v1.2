'use client';

import Link from 'next/link';
import { ProductDto } from '@/types/product';
import { motion } from 'framer-motion';
import { Box } from 'lucide-react';
import { Card } from '@/ui/card';

interface ProductCardProps {
  product: ProductDto;
}

export default function ProductCard({ product }: ProductCardProps) {
  return (
    <Link href={`/products/${product.id}`}>
      <motion.div
        whileHover={{ y: -4 }}
        transition={{ duration: 0.4, ease: [0.4, 0, 0.2, 1] }}
        className="h-full"
      >
        <Card className="h-full flex flex-col group cursor-pointer">
          {/* Image Container */}
          <div className="relative aspect-square bg-[#F5F5F7] overflow-hidden">
          {product.imageUrl ? (
            <img
              src={`http://localhost:8081${product.imageUrl}`}
              alt={product.name}
              className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center">
              <Box className="w-16 h-16 text-[#86868B]" />
            </div>
          )}
          
          {/* 3D Badge */}
          {product.model3dUrl && (
            <div className="absolute top-3 right-3 bg-[#007AFF] text-white text-[11px] font-medium px-2 py-1 rounded-full">
              3D
            </div>
          )}
          </div>

          {/* Content */}
          <div className="p-5 flex flex-col flex-grow">
          <h3 className="text-title text-base text-[#1D1D1F] mb-2 line-clamp-2 group-hover:text-[#007AFF] transition-colors">
            {product.name}
          </h3>
          
          {product.description && (
            <p className="text-body text-xs text-[#86868B] mb-4 line-clamp-2 flex-grow">
              {product.description}
            </p>
          )}

          {/* Price and Info */}
          <div className="flex items-center justify-between mt-auto pt-4 border-t border-[#E5E5E7]">
            <div>
              <span className="text-title text-lg text-[#1D1D1F]">
                ${product.price.toFixed(2)}
              </span>
            </div>
            {product.sellerName && (
              <span className="text-xs text-[#86868B]">
                {product.sellerName}
              </span>
            )}
          </div>
          </div>
        </Card>
      </motion.div>
    </Link>
  );
}
