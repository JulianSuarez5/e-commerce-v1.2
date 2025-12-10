'use client';

import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import AppleHeader from '@/components/AppleHeader';
import { ProductCard } from '@/components/ProductCard';
import { ProductDto, Category, Brand } from '@/types/product';
import { Filter, X } from 'lucide-react';
import { Container } from '@/ui/container';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';

export default function ProductsPage() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [brands, setBrands] = useState<Brand[]>([]);
  const [loading, setLoading] = useState(true);
  const [filtersOpen, setFiltersOpen] = useState(false);
  const [filters, setFilters] = useState({
    categoryId: null as number | null,
    brandId: null as number | null,
    query: '',
    minPrice: null as number | null,
    maxPrice: null as number | null,
  });

  useEffect(() => {
    fetchCategories();
    fetchBrands();
    fetchProducts();
  }, [filters]);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams();
      if (filters.categoryId) params.append('categoryId', filters.categoryId.toString());
      if (filters.brandId) params.append('brandId', filters.brandId.toString());
      if (filters.query) params.append('q', filters.query);
      if (filters.minPrice) params.append('minPrice', filters.minPrice.toString());
      if (filters.maxPrice) params.append('maxPrice', filters.maxPrice.toString());

      const response = await fetch(`/api/products?${params.toString()}`);
      const data = await response.json();
      setProducts(data);
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await fetch('/api/categories');
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchBrands = async () => {
    try {
      const response = await fetch('/api/brands');
      const data = await response.json();
      setBrands(data);
    } catch (error) {
      console.error('Error fetching brands:', error);
    }
  };

  const clearFilters = () => {
    setFilters({
      categoryId: null,
      brandId: null,
      query: '',
      minPrice: null,
      maxPrice: null,
    });
  };

  const hasActiveFilters = filters.categoryId || filters.brandId || filters.query || filters.minPrice || filters.maxPrice;

  return (
    <div className="min-h-screen bg-[#FFFFFF]">
      <AppleHeader />

      <Container className="pt-24 pb-16">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-display mb-2">
            Productos
          </h1>
          <p className="text-subtitle">
            {products.length} productos encontrados
          </p>
        </div>

        {/* Search and Filters */}
        <div className="mb-8 space-y-4">
          {/* Search Bar */}
          <div className="relative">
            <Input
              type="text"
              placeholder="Buscar productos..."
              value={filters.query}
              onChange={(e) => setFilters({ ...filters, query: e.target.value })}
              className="pl-12"
            />
            <Filter className="absolute left-4 top-1/2 transform -translate-y-1/2 w-5 h-5 text-[#86868B]" />
          </div>

          {/* Active Filters */}
          {hasActiveFilters && (
            <div className="flex items-center space-x-2 flex-wrap">
              {filters.categoryId && (
                <span className="inline-flex items-center space-x-2 bg-[#F5F5F7] px-3 py-1 rounded-full text-xs">
                  <span>Categoría: {categories.find(c => c.id === filters.categoryId)?.name}</span>
                  <button onClick={() => setFilters({ ...filters, categoryId: null })}>
                    <X className="w-3 h-3" />
                  </button>
                </span>
              )}
              {filters.brandId && (
                <span className="inline-flex items-center space-x-2 bg-[#F5F5F7] px-3 py-1 rounded-full text-xs">
                  <span>Marca: {brands.find(b => b.id === filters.brandId)?.name}</span>
                  <button onClick={() => setFilters({ ...filters, brandId: null })}>
                    <X className="w-3 h-3" />
                  </button>
                </span>
              )}
              <button
                onClick={clearFilters}
                className="text-body text-[#007AFF] hover:opacity-80"
              >
                Limpiar filtros
              </button>
            </div>
          )}

          {/* Filter Buttons */}
          <div className="flex space-x-3">
            <Button
              onClick={() => setFiltersOpen(!filtersOpen)}
              variant="secondary"
              size="md"
              className="flex items-center space-x-2"
            >
              <Filter className="w-4 h-4" />
              <span>Filtros</span>
            </Button>
          </div>

          {/* Filters Panel */}
          {filtersOpen && (
            <motion.div
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: 'auto' }}
              exit={{ opacity: 0, height: 0 }}
              className="space-y-4"
            >
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Categoría
                  </label>
                  <select
                    value={filters.categoryId || ''}
                    onChange={(e) => setFilters({ ...filters, categoryId: e.target.value ? Number(e.target.value) : null })}
                    className="w-full rounded-[10px] border border-[#E5E5E7] bg-[#F5F5F7] px-4 py-3 text-sm text-[#1D1D1F] outline-none transition-all duration-200 ease-[0.4,0,0.2,1] focus:border-[#007AFF] focus:ring-4 focus:ring-[rgba(0,122,255,0.12)]"
                  >
                    <option value="">Todas</option>
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Marca
                  </label>
                  <select
                    value={filters.brandId || ''}
                    onChange={(e) => setFilters({ ...filters, brandId: e.target.value ? Number(e.target.value) : null })}
                    className="w-full rounded-[10px] border border-[#E5E5E7] bg-[#F5F5F7] px-4 py-3 text-sm text-[#1D1D1F] outline-none transition-all duration-200 ease-[0.4,0,0.2,1] focus:border-[#007AFF] focus:ring-4 focus:ring-[rgba(0,122,255,0.12)]"
                  >
                    <option value="">Todas</option>
                    {brands.map((brand) => (
                      <option key={brand.id} value={brand.id}>
                        {brand.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Precio Mínimo
                  </label>
                  <Input
                    type="number"
                    placeholder="0"
                    value={filters.minPrice || ''}
                    onChange={(e) => setFilters({ ...filters, minPrice: e.target.value ? Number(e.target.value) : null })}
                    className=""
                  />
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#86868B] mb-2">
                    Precio Máximo
                  </label>
                  <Input
                    type="number"
                    placeholder="9999"
                    value={filters.maxPrice || ''}
                    onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value ? Number(e.target.value) : null })}
                    className=""
                  />
                </div>
              </div>
            </motion.div>
          )}
        </div>

        {/* Products Grid */}
        {loading ? (
          <div className="text-center py-16">
            <div className="w-12 h-12 border-4 border-[#E5E5E7] border-t-[#007AFF] rounded-full animate-spin mx-auto mb-4" />
            <p className="text-body text-[#86868B]">Cargando productos...</p>
          </div>
        ) : products.length === 0 ? (
          <div className="text-center py-16">
            <p className="text-title text-lg text-[#1D1D1F] mb-2">
              No se encontraron productos
            </p>
            <p className="text-body text-[#86868B]">
              Intenta ajustar tus filtros de búsqueda
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {products.map((product, index) => (
              <motion.div
                key={product.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ 
                  duration: 0.4, 
                  delay: index * 0.05,
                  ease: [0.4, 0, 0.2, 1]
                }}
              >
                <ProductCard product={product} />
              </motion.div>
            ))}
          </div>
        )}
      </Container>
    </div>
  );
}
