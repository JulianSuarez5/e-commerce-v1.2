'use client';

import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';

export default function AdminBrandsPage() {
  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-display mb-1">Marcas</h1>
        <p className="text-subtitle">
          Panel de marcas preparado para integrarse con la API existente.
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
                Conecta esta tabla a `/api/brands` para ver y administrar las
                marcas reales del catálogo.
              </TD>
            </TR>
          </TBody>
        </Table>
      </Card>
    </div>
  );
}


