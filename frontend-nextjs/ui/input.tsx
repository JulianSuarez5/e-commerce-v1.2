import type { InputHTMLAttributes } from 'react';
import { cn } from '@/lib/cn';

export interface InputProps extends InputHTMLAttributes<HTMLInputElement> {}

export function Input({ className, ...props }: InputProps) {
  return (
    <input
      className={cn(
        'w-full rounded-[10px] border border-[#E5E5E7] bg-[#F5F5F7] px-4 py-3 text-sm text-[#1D1D1F] outline-none transition-all duration-200 ease-[0.4,0,0.2,1] focus:border-[#007AFF] focus:ring-4 focus:ring-[rgba(0,122,255,0.12)] placeholder:text-[#86868B]',
        className
      )}
      {...props}
    />
  );
}


