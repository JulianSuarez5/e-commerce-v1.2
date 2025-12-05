'use client';

import { useEffect, useState } from 'react';
import { ProductDto } from '@/types/product';
import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';
import { Button } from '@/ui/button';
import { Modal } from '@/ui/modal';
import { Input } from '@/ui/input';

export default function AdminProductsPage() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await fetch('/api/products');
      if (res.ok) {
        const data = await res.json();
        setProducts(data);
      }
    } catch {
      // silencioso: la tabla explica que es una vista conectada a la API pública
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <header className="flex items-center justify-between gap-3">
        <div>
          <h1 className="text-display mb-1">Productos</h1>
          <p className="text-subtitle">
            Gestión básica de productos usando la API pública existente.
          </p>
        </div>
        <Button variant="primary" onClick={() => setModalOpen(true)}>
          Nuevo producto
        </Button>
      </header>

      <Card className="p-5">
        <Table>
          <THead>
            <TR>
              <TH>ID</TH>
              <TH>Nombre</TH>
              <TH>Categoría</TH>
              <TH>Marca</TH>
              <TH className="text-right">Precio</TH>
            </TR>
          </THead>
          <TBody>
            {loading ? (
              <TR>
                <TD colSpan={5} className="text-center text-xs text-[#86868B] py-6">
                  Cargando productos…
                </TD>
              </TR>
            ) : products.length === 0 ? (
              <TR>
                <TD colSpan={5} className="text-center text-xs text-[#86868B] py-6">
                  No hay productos disponibles. Usa el backend para crear
                  productos y se verán aquí.
                </TD>
              </TR>
            ) : (
              products.map((p) => (
                <TR key={p.id}>
                  <TD>{p.id}</TD>
                  <TD>{p.name}</TD>
                  <TD>{p.categoryName ?? '-'}</TD>
                  <TD>{p.brandName ?? '-'}</TD>
                  <TD className="text-right">${p.price.toFixed(2)}</TD>
                </TR>
              ))
            )}
          </TBody>
        </Table>
      </Card>

      <Modal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        title="Nuevo producto (placeholder)"
        footer={
          <>
            <Button variant="secondary" onClick={() => setModalOpen(false)}>
              Cancelar
            </Button>
            <Button disabled>Guardar (conectar a API)</Button>
          </>
        }
      >
        <p className="text-xs text-[#86868B] mb-4">
          Esta vista está preparada para conectarse a un endpoint de creación de
          productos en el backend. Por ahora funciona sólo como maqueta de
          diseño.
        </p>
        <div className="space-y-3">
          <div>
            <label className="block text-xs font-medium text-[#86868B] mb-1">
              Nombre
            </label>
            <Input placeholder="Nombre del producto" />
          </div>
          <div>
            <label className="block text-xs font-medium text-[#86868B] mb-1">
              Precio
            </label>
            <Input type="number" placeholder="0.00" />
          </div>
        </div>
      </Modal>
    </div>
  );
}


