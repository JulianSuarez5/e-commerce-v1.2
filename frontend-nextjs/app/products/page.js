import Link from 'next/link';
import products from '../../mock-data/products.json';

export default function ProductsPage() {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Products</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {products.map(product => (
          <div key={product.id} className="border rounded-lg p-4">
            <Link href={`/products/${product.id}`}>
              <h2 className="text-lg font-semibold">{product.name}</h2>
              <p className="text-gray-700">${product.price}</p>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
}
