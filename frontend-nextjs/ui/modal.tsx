import type { PropsWithChildren, ReactNode } from 'react';
import { cn } from '@/lib/cn';

interface ModalProps {
  open: boolean;
  title?: string;
  onClose: () => void;
  footer?: ReactNode;
}

export function Modal({
  open,
  title,
  onClose,
  footer,
  children,
}: PropsWithChildren<ModalProps>) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/10 backdrop-blur-sm">
      <div className="w-full max-w-lg rounded-[16px] border border-[#E5E5E7] bg-white shadow-lg">
        {title && (
          <div className="border-b border-[#E5E5E7] px-6 py-4">
            <h2 className="text-title text-base">{title}</h2>
          </div>
        )}
        <div className="px-6 py-4">{children}</div>
        {footer && (
          <div className={cn('border-t border-[#E5E5E7] px-6 py-4 flex justify-end gap-3')}>
            {footer}
          </div>
        )}
      </div>
      <button
        aria-label="Cerrar"
        onClick={onClose}
        className="fixed inset-0 cursor-default"
      />
    </div>
  );
}


