import type { HTMLAttributes, PropsWithChildren, ThHTMLAttributes, TdHTMLAttributes } from 'react';
import { cn } from '@/lib/cn';

export function Table({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLTableElement>>) {
  return (
    <table
      className={cn('w-full border-collapse text-sm text-[#1D1D1F]', className)}
      {...props}
    />
  );
}

export function THead({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLTableSectionElement>>) {
  return (
    <thead
      className={cn('border-b border-[#E5E5E7] bg-[#F5F5F7]', className)}
      {...props}
    />
  );
}

export function TBody({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLTableSectionElement>>) {
  return <tbody className={cn('[&_tr:last-child]:border-b-0', className)} {...props} />;
}

export function TR({
  className,
  ...props
}: PropsWithChildren<HTMLAttributes<HTMLTableRowElement>>) {
  return (
    <tr
      className={cn(
        'border-b border-[#E5E5E7] hover:bg-[#F5F5F7]/50 transition-colors',
        className
      )}
      {...props}
    />
  );
}

export function TH({
  className,
  ...props
}: PropsWithChildren<ThHTMLAttributes<HTMLTableCellElement>>) {
  return (
    <th
      className={cn(
        'px-4 py-3 text-left text-xs font-medium uppercase tracking-wide text-[#86868B]',
        className
      )}
      {...props}
    />
  );
}

export function TD({
  className,
  ...props
}: PropsWithChildren<TdHTMLAttributes<HTMLTableCellElement>>) {
  return (
    <td
      className={cn('px-4 py-3 align-middle text-sm text-[#1D1D1F]', className)}
      {...props}
    />
  );
}
