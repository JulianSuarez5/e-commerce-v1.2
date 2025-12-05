# PR: feat/marketplace-sellers

## 📋 Descripción

Implementación completa del sistema de vendedores (Sellers) para habilitar funcionalidad de marketplace tipo MercadoLibre.

## 🎯 Cambios Realizados

### Backend

#### Nuevos Modelos
- **Seller.java**: Entidad principal con información del negocio, estadísticas (rating, ventas, revenue), y relación con User
- **Product.java**: Agregado campo `seller` para asociar productos con vendedores

#### Nuevos DTOs
- **SellerDto.java**: DTO completo para transferencia de datos
- **CreateSellerRequest.java**: Request DTO para crear vendedores con validaciones

#### Nuevos Repositorios
- **SellerRepository.java**: Repositorio JPA con queries personalizadas

#### Nuevos Servicios
- **SellerService.java**: Interfaz del servicio
- **SellerServiceImpl.java**: Implementación con lógica de negocio:
  - Creación de vendedores
  - Actualización automática de rol a SELLER
  - Cálculo de estadísticas
  - Validaciones

#### Nuevos Controllers
- **SellerApiController.java**: REST API completa:
  - `POST /api/sellers` - Crear vendedor (requiere autenticación)
  - `GET /api/sellers/{id}` - Obtener vendedor por ID
  - `GET /api/sellers/user/{userId}` - Obtener vendedor por userId
  - `GET /api/sellers/{id}/products` - Productos del vendedor
  - `GET /api/sellers` - Listar vendedores (con filtros verified/active)
  - `PUT /api/sellers/{id}` - Actualizar vendedor (solo owner o admin)

#### Mappers
- **SellerMapper.java**: MapStruct mapper para conversión Entity ↔ DTO

#### Actualizaciones
- **ProductRepository.java**: Agregado `countBySellerId` y `findBySellerId`
- **SecurityConfig.java**: Agregado `/api/sellers/**` a rutas públicas (GET)

## 🔐 Seguridad

- Solo usuarios autenticados pueden crear vendedores
- Solo el dueño del vendedor o admin pueden actualizar
- Asignación automática de rol SELLER al crear vendedor
- Validación de que un usuario no puede tener múltiples sellers

## 📊 Base de Datos

### Nueva Tabla: `sellers`
```sql
CREATE TABLE sellers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    business_name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    logo_url VARCHAR(500),
    website VARCHAR(255),
    tax_id VARCHAR(100),
    phone VARCHAR(50),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    rating DOUBLE DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    total_sales INT DEFAULT 0,
    total_revenue DOUBLE DEFAULT 0.0,
    active BOOLEAN DEFAULT TRUE,
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Modificación: `products`
```sql
ALTER TABLE products ADD COLUMN seller_id INT;
ALTER TABLE products ADD FOREIGN KEY (seller_id) REFERENCES sellers(id);
```

## 🧪 Testing

### Tests Unitarios (Pendiente)
- [ ] SellerServiceTest - Crear, actualizar, eliminar vendedores
- [ ] SellerApiControllerTest - Endpoints REST
- [ ] SellerMapperTest - Conversiones DTO

### Tests de Integración (Pendiente)
- [ ] Crear vendedor completo flow
- [ ] Asignación automática de rol
- [ ] Validación de permisos

## 📝 Ejemplos de Uso

### Crear Vendedor
```bash
POST /api/sellers
Authorization: Bearer {token}
Content-Type: application/json

{
  "businessName": "Mi Tienda Online",
  "description": "Vendemos productos de calidad",
  "website": "https://mitienda.com",
  "taxId": "123456789",
  "phone": "+1234567890",
  "address": "Calle Principal 123",
  "city": "Ciudad",
  "state": "Estado",
  "zipCode": "12345",
  "country": "País"
}
```

### Obtener Vendedor
```bash
GET /api/sellers/1
```

### Listar Vendedores Verificados
```bash
GET /api/sellers?verified=true
```

## ✅ Checklist

- [x] Modelo Seller creado
- [x] DTOs creados
- [x] Repository creado
- [x] Service implementado
- [x] Controller REST creado
- [x] Mapper MapStruct creado
- [x] Seguridad configurada
- [x] Relación Product-Seller agregada
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Documentación OpenAPI/Swagger
- [ ] Migración DB (Flyway/Liquibase)

## 🚀 Comandos para Probar

```bash
# 1. Compilar proyecto
mvn clean install

# 2. Iniciar servicios
docker-compose up -d

# 3. Ejecutar aplicación
mvn spring-boot:run

# 4. Probar endpoints
curl -X POST http://localhost:8081/api/sellers \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "businessName": "Test Store",
    "description": "Test description"
  }'

curl http://localhost:8081/api/sellers
```

## 📌 Notas

- El rol SELLER se asigna automáticamente al crear un vendedor
- Los productos pueden asociarse a vendedores (campo seller agregado)
- Las estadísticas (rating, sales, revenue) se actualizarán en futuras features
- La verificación de vendedores será manual por admin (pendiente implementar)

## 🔗 Issues Relacionados

- #1 - Sistema de marketplace
- #2 - Roles y permisos avanzados

---

**Labels:** `feature`, `backend`, `marketplace`
**Tiempo estimado:** 4-6 horas
**Prioridad:** Alta

