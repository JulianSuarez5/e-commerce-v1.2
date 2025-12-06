'use client';

import Product3DViewer from '@/components/Product3DViewer';
import { Container } from '@/ui/container';
import { Section } from '@/ui/section';
import { ProductDto } from '@/types/product'; // Import the type for safety

// Create a placeholder product that matches the ProductDto type
const placeholderProduct: ProductDto = {
  id: 0,
  name: 'Placeholder Product',
  description: 'This is a placeholder product description.',
  price: 999.99,
  cantidad: 10, // Changed from 'stock' to 'cantidad'
  imageUrl: null,
  model3dUrl: null, // This will safely show the fallback message in the viewer
  active: true,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  categoryId: null,
  categoryName: 'Electronics',
  brandId: null,
  brandName: 'BrandName',
  sellerId: null,
  sellerName: 'SellerName',
  images: [],
  variants: [],
};

export default function HomePage() {
  return (
    <Container>
      <Section className="relative -mt-20 md:-mt-24">
        <div className="text-center">
          <h1 className="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl">
            Innovación a tu Alcance
          </h1>
          <p className="mt-6 text-lg leading-8 text-gray-600">
            Descubre una nueva era de compras online. Calidad y diseño premium en cada producto.
          </p>
        </div>
        {/* Pass the placeholder product to the component */}
        <Product3DViewer product={placeholderProduct} />
      </Section>
    </Container>
  );
}
