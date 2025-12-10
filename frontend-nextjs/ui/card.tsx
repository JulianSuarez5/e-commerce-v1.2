import { cn } from '@/lib/cn';
import type { HTMLAttributes, PropsWithChildren } from 'react';

export function Card({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) {
  return (
    <div
      className={cn(
        'bg-white border border-[#E5E5E7] rounded-[12px] shadow-sm transition-transform duration-300 ease-[0.4,0,0.2,1] hover:-translate-y-1 hover:shadow-lg',
        className
      )}
      {...props}
    />
  );
}


