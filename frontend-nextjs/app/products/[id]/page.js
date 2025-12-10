import { notFound } from 'next/navigation';
import products from '../../../mock-data/products.json';

function getProduct(id) {
  return products.find((p) => p.id === id);
}

export default function ProductDetailPage({ params }) {
  const product = getProduct(params.id);

  if (!product) {
    notFound();
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">{product.name}</h1>
      <p className="text-xl text-gray-700 mb-4">${product.price}</p>
      <p className="text-gray-600">{product.description}</p>
      {/* Add more product details here as needed */}
    </div>
  );
}
