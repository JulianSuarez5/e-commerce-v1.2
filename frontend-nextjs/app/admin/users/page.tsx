'use client';

import { Card } from '@/ui/card';
import { Table, THead, TBody, TR, TH, TD } from '@/ui/table';

export default function AdminUsersPage() {
  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-display mb-1">Usuarios</h1>
        <p className="text-subtitle">
          Maqueta de gestión de usuarios lista para conectarse a la API.
        </p>
      </header>

      <Card className="p-5">
        <Table>
          <THead>
            <TR>
              <TH>ID</TH>
              <TH>Nombre</TH>
              <TH>Email</TH>
              <TH>Rol</TH>
            </TR>
          </THead>
          <TBody>
            <TR>
              <TD colSpan={4} className="text-center text-xs text-[#86868B] py-6">
                Cuando exista un endpoint de administración de usuarios, conecta
                esta tabla a la API para listar, filtrar y editar usuarios.
              </TD>
            </TR>
          </TBody>
        </Table>
      </Card>
    </div>
  );
}


