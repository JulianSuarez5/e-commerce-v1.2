export interface CartItemDto {
  id: number;
  productId: number;
  productName: string;
  productImageUrl: string | null;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface CartDto {
  id: number;
  userId: number;
  username: string;
  items: CartItemDto[];
  totalPrice: number;
  totalItems: number;
  createdAt: string;
  updatedAt: string;
}

