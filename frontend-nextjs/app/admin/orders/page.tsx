'use client';

import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';

export default function AdminOrdersPage() {
  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-display mb-1">Órdenes</h1>
        <p className="text-subtitle">
          Vista pensada para enlazar con el endpoint de órdenes del backend.
        </p>
      </header>

      <Card className="p-5">
        <Table>
          <THead>
            <TR>
              <TH>Número</TH>
              <TH>Cliente</TH>
              <TH>Estado</TH>
              <TH className="text-right">Total</TH>
            </TR>
          </THead>
          <TBody>
            <TR>
              <TD colSpan={4} className="text-center text-xs text-[#86868B] py-6">
                Integra esta tabla con la API de órdenes (`/api/orders` o
                equivalente) para ver el historial y estado de cada pedido.
              </TD>
            </TR>
          </TBody>
        </Table>
      </Card>
    </div>
  );
}


