'use client';

import { useState } from 'react';
import { Input } from '@/ui/input';
import { Button } from '@/ui/button';
import { Mail, Lock } from 'lucide-react';

interface AuthFormProps {
  onSubmit: (credentials: { email: string; password: string }) => void;
  loading: boolean;
  submitText: string;
}

export function AuthForm({ onSubmit, loading, submitText }: AuthFormProps) {
  const [credentials, setCredentials] = useState({ email: '', password: '' });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSubmit(credentials);
  };

  return (
    <div className="relative rounded-[16px] border border-[#E5E5E7] bg-white p-8 shadow-sm">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="relative">
          <Mail className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
          <Input
            type="email"
            name="email"
            value={credentials.email}
            onChange={handleChange}
            placeholder="tu@email.com"
            className="pl-12"
            required
          />
        </div>
        <div className="relative">
          <Lock className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[#86868B]" />
          <Input
            type="password"
            name="password"
            value={credentials.password}
            onChange={handleChange}
            placeholder="Contraseña"
            className="pl-12"
            required
          />
        </div>
        <Button type="submit" disabled={loading} fullWidth size="lg">
          {loading ? 'Cargando...' : submitText}
        </Button>
      </form>
    </div>
  );
}
