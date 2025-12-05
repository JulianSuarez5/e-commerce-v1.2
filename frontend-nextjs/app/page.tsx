'use client';

import { HeroOrbitModel } from '@/components/HeroOrbitModel';
import { ProductCard } from '@/components/ProductCard';
import { Container } from '@/ui/container';
import { Section } from '@/ui/section';
import { useProducts } from '@/hooks/useProducts';
import { Skeleton } from '@/ui/skeleton';

export default function HomePage() {
  const { products, loading } = useProducts();

  return (
    <Container>
      <Section className="relative -mt-20 md:-mt-24">
        <HeroOrbitModel />
      </Section>

      <Section>
        <div className="text-center">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            Recién Llegados
          </h2>
          <p className="mt-4 text-lg text-gray-600">
            Explora nuestras últimas novedades y encuentra la tecnología que te inspira.
          </p>
        </div>

        <div className="mt-12 grid grid-cols-1 gap-y-10 sm:grid-cols-2 lg:grid-cols-4 xl:gap-x-8">
          {loading
            ? Array.from({ length: 4 }).map((_, i) => (
                <div key={i} className="space-y-4">
                  <Skeleton className="h-64" />
                  <Skeleton className="h-4 w-2/3" />
                  <Skeleton className="h-4 w-1/2" />
                </div>
              ))
            : products.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
        </div>
      </Section>
    </Container>
  );
}
