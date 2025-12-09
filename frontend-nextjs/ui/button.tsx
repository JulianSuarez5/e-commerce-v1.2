'use client';

import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/cn';
import { Slot } from '@radix-ui/react-slot';
import type { ButtonHTMLAttributes } from 'react';

const buttonStyles = cva(
  'inline-flex items-center justify-center whitespace-nowrap font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-[#007AFF] disabled:opacity-50 disabled:cursor-not-allowed',
  {
    variants: {
      variant: {
        primary:
          'bg-[#007AFF] text-white hover:bg-[#0A84FF] shadow-sm hover:shadow-md rounded-[8px] border border-transparent',
        secondary:
          'bg-white text-[#1D1D1F] border border-[#E5E5E7] hover:bg-[#F5F5F7] rounded-[8px] shadow-sm',
        ghost:
          'bg-transparent text-[#1D1D1F] hover:bg-[#F5F5F7] rounded-[8px] border border-transparent',
      },
      size: {
        sm: 'h-8 px-3 text-sm',
        md: 'h-10 px-4 text-sm',
        lg: 'h-11 px-5 text-base',
      },
      fullWidth: {
        true: 'w-full',
        false: '',
      },
    },
    defaultVariants: {
      variant: 'primary',
      size: 'md',
      fullWidth: false,
    },
  }
);

export interface ButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonStyles> {
      asChild?: boolean;
    }

export function Button({
  variant,
  size,
  fullWidth,
  className,
  asChild = false,
  ...props
}: ButtonProps) {
  const Comp = asChild ? Slot : 'button';
  return (
    <Comp
      className={cn(buttonStyles({ variant, size, fullWidth, className }))}
      {...props}
    />
  );
}


