'use client';

import { Product3DViewer } from '@/components/Product3DViewer';
import { Container } from '@/ui/container';
import { Section } from '@/ui/section';

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
        <Product3DViewer />
      </Section>
    </Container>
  );
}
