export interface ProductImageDto {
  id: number;
  url: string;
  thumbnailUrl: string | null;
  displayOrder: number;
  isPrimary: boolean;
}

export interface ProductVariantDto {
  id: number;
  name: string;
  sku: string | null;
  color: string | null;
  size: string | null;
  priceModifier: number | null;
  stock: number;
  imageUrl: string | null;
  active: boolean;
}

export interface ProductModel3DDto {
  id: number;
  url: string;
  format: string;
  fileSize: number | null;
  thumbnailUrl: string | null;
  optimized: boolean;
}

export interface ProductDto {
  id: number;
  name: string;
  description: string;
  price: number;
  cantidad: number;
  imageUrl: string | null;
  model3dUrl: string | null;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  categoryId: number | null;
  categoryName: string | null;
  brandId: number | null;
  brandName: string | null;
  sellerId: number | null;
  sellerName: string | null;
  images?: ProductImageDto[];
  variants?: ProductVariantDto[];
  primaryModel3D?: ProductModel3DDto;
}

export interface Category {
  id: number;
  name: string;
  description: string | null;
  imageUrl: string | null;
  active: boolean;
}

export interface Brand {
  id: number;
  name: string;
  description: string | null;
  logoUrl: string | null;
  website: string | null;
  active: boolean;
}

