# PR: feat/cart-checkout

## 📋 Descripción

Implementación completa de carrito persistente y checkout con validación de stock, creación de órdenes y limpieza automática del carrito.

## 🎯 Cambios Realizados

### Nuevos Controllers

#### CartApiController
- `GET /api/cart` - Obtener carrito del usuario autenticado
- `POST /api/cart/items` - Agregar item al carrito
- `PUT /api/cart/items/{itemId}` - Actualizar cantidad de item
- `DELETE /api/cart/items/{itemId}` - Eliminar item del carrito
- `DELETE /api/cart` - Vaciar carrito completo

#### CheckoutApiController
- `POST /api/checkout` - Procesar checkout completo:
  - Validar stock de todos los items
  - Crear orden con número único
  - Crear detalles de orden
  - Reducir stock de productos
  - Limpiar carrito
  - Crear pago simulado

### Actualizaciones de Servicios

#### CartService
- Agregado `getOrCreateCart(Integer userId)` - Sobrecarga para facilitar uso
- Agregado `addItem(Integer cartId, Integer productId, Integer quantity)` - Método alternativo
- Agregado `clearCart(Integer userId)` - Sobrecarga

#### CartServiceImpl
- Implementación de métodos sobrecargados
- Integración con UserRepository y ProductRepository

### Nuevos DTOs

#### CheckoutRequest
- Validaciones de dirección de envío
- Método de pago
- Notas opcionales

## 🔐 Seguridad

- Todos los endpoints requieren autenticación
- Validación de propiedad del carrito (solo el dueño puede modificar)
- Validación de stock antes de checkout
- Validación de productos activos

## 📊 Flujo de Checkout

1. Usuario autenticado accede a `/api/checkout`
2. Se valida que el carrito no esté vacío
3. Se valida stock de cada producto
4. Se calcula el total
5. Se crea la orden con número único (ORD-XXXXXXXX)
6. Se crean los detalles de orden
7. Se reduce el stock de cada producto
8. Se guarda la orden
9. Se limpia el carrito
10. Se crea pago simulado (PENDING)

## 📝 Ejemplos de Uso

### Agregar Item al Carrito
```bash
POST /api/cart/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

### Obtener Carrito
```bash
GET /api/cart
Authorization: Bearer {token}
```

### Checkout
```bash
POST /api/checkout
Authorization: Bearer {token}
Content-Type: application/json

{
  "shippingAddress": "Calle Principal 123",
  "shippingCity": "Ciudad",
  "shippingState": "Estado",
  "shippingZipCode": "12345",
  "paymentMethod": "PAYPAL",
  "notes": "Entregar en horario laboral"
}
```

## ✅ Checklist

- [x] CartApiController creado
- [x] CheckoutApiController creado
- [x] Validación de stock
- [x] Creación de órdenes
- [x] Reducción de stock automática
- [x] Limpieza de carrito post-checkout
- [x] DTOs creados
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Manejo de errores mejorado
- [ ] Integración con servicio de pagos real

## 🚀 Comandos para Probar

```bash
# 1. Agregar items al carrito
curl -X POST http://localhost:8081/api/cart/items \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'

# 2. Ver carrito
curl http://localhost:8081/api/cart \
  -H "Authorization: Bearer {token}"

# 3. Procesar checkout
curl -X POST http://localhost:8081/api/checkout \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "shippingAddress": "Test Address",
    "shippingCity": "Test City",
    "shippingState": "Test State",
    "shippingZipCode": "12345",
    "paymentMethod": "PAYPAL"
  }'
```

## 📌 Notas

- El carrito se persiste en base de datos para usuarios autenticados
- El número de orden se genera automáticamente con formato ORD-XXXXXXXX
- El stock se reduce inmediatamente al procesar el checkout
- El pago se crea con estado PENDING (pendiente integración con gateway real)
- La orden se crea con estado CREATED

## 🔗 Issues Relacionados

- #6 - Carrito persistente
- #7 - Checkout flow
- #8 - Integración de pagos

---

**Labels:** `feature`, `backend`, `cart`, `checkout`
**Tiempo estimado:** 4-6 horas
**Prioridad:** Alta

