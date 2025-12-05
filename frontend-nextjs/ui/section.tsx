import type { HTMLAttributes, PropsWithChildren } from 'react';
import { cn } from '@/lib/cn';

export function Section({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLElement>>) {
  return (
    <section
      className={cn('py-12 sm:py-16 lg:py-20', className)}
      {...props}
    />
  );
}


