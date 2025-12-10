'use client';

import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';

export default function AdminCategoriesPage() {
  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-display mb-1">Categorías</h1>
        <p className="text-subtitle">
          Gestiona la estructura de categorías del catálogo cuando conectes esta
          vista a la API.
        </p>
      </header>

      <Card className="p-5">
        <Table>
          <THead>
            <TR>
              <TH>ID</TH>
              <TH>Nombre</TH>
              <TH>Estado</TH>
            </TR>
          </THead>
          <TBody>
            <TR>
              <TD colSpan={3} className="text-center text-xs text-[#86868B] py-6">
                Conecta esta tabla a `/api/categories` para mostrar las
                categorías reales del backend.
              </TD>
            </TR>
          </TBody>
        </Table>
      </Card>
    </div>
  );
}


