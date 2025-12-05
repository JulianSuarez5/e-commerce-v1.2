'use client';

import ProtectedRoute from '@/components/ProtectedRoute';
import { Container } from '@/ui/container';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/cn';
import { LayoutDashboard, Package, Users, ShoppingBag, Tag, Boxes } from 'lucide-react';

const navItems = [
  { href: '/admin', label: 'Resumen', icon: LayoutDashboard },
  { href: '/admin/products', label: 'Productos', icon: Package },
  { href: '/admin/orders', label: 'Órdenes', icon: ShoppingBag },
  { href: '/admin/users', label: 'Usuarios', icon: Users },
  { href: '/admin/categories', label: 'Categorías', icon: Tag },
  { href: '/admin/brands', label: 'Marcas', icon: Boxes },
];

export default function AdminLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();

  return (
    <ProtectedRoute requiredRole="ADMIN">
      <div className="min-h-screen bg-[#FFFFFF]">
        <div className="flex">
          {/* Sidebar */}
          <aside className="hidden md:flex md:w-64 lg:w-72 border-r border-[#E5E5E7] bg-white/90 backdrop-blur-xl">
            <div className="flex flex-col w-full">
              <div className="px-6 py-5 border-b border-[#E5E5E7]">
                <span className="block text-xs uppercase tracking-[0.16em] text-[#86868B] mb-1">
                  Panel
                </span>
                <span className="text-title text-base text-[#1D1D1F]">
                  Administración
                </span>
              </div>
              <nav className="flex-1 px-3 py-4 space-y-1">
                {navItems.map((item) => {
                  const Icon = item.icon;
                  const active = pathname === item.href;
                  return (
                    <Link
                      key={item.href}
                      href={item.href}
                      className={cn(
                        'flex items-center gap-3 rounded-[10px] px-3 py-2 text-sm transition-colors',
                        active
                          ? 'bg-[#F5F5F7] text-[#1D1D1F]'
                          : 'text-[#86868B] hover:bg-[#F5F5F7]'
                      )}
                    >
                      <Icon className="w-4 h-4" />
                      <span>{item.label}</span>
                    </Link>
                  );
                })}
              </nav>
            </div>
          </aside>

          {/* Main content */}
          <main className="flex-1">
            <Container className="pt-8 pb-12">
              {children}
            </Container>
          </main>
        </div>
      </div>
    </ProtectedRoute>
  );
}


