# PR: feat/product-variants-and-media

## 📋 Descripción

Implementación completa de variantes de productos, múltiples imágenes y upload de modelos 3D (GLB/GLTF) para productos.

## 🎯 Cambios Realizados

### Nuevos Modelos

#### ProductVariant
- Variantes de producto (color, tamaño, material)
- SKU único
- Modificador de precio
- Stock independiente
- Imagen por variante

#### ProductImage
- Múltiples imágenes por producto
- Orden de visualización
- Imagen principal
- Thumbnails

#### ProductModel3D
- Modelos 3D (GLB/GLTF)
- Tamaño de archivo
- Estado de optimización (DRACO)
- Thumbnail/preview

### Nuevos Servicios

#### FileStorageService
- Upload de imágenes (JPEG, PNG, WebP)
- Upload de modelos 3D (GLB, GLTF)
- Validación de tipos MIME y tamaños
- Generación de thumbnails (placeholder)
- Eliminación de archivos

### Nuevos Controllers

#### ProductMediaApiController
- `POST /api/products/{id}/images` - Subir imagen
- `GET /api/products/{id}/images` - Listar imágenes
- `DELETE /api/products/{id}/images/{imageId}` - Eliminar imagen
- `POST /api/products/{id}/models3d` - Subir modelo 3D
- `GET /api/products/{id}/models3d` - Listar modelos 3D
- `DELETE /api/products/{id}/models3d/{modelId}` - Eliminar modelo 3D

#### ProductVariantApiController
- `POST /api/products/{id}/variants` - Crear variante
- `GET /api/products/{id}/variants` - Listar variantes
- `GET /api/products/{id}/variants/{variantId}` - Obtener variante
- `PUT /api/products/{id}/variants/{variantId}` - Actualizar variante
- `DELETE /api/products/{id}/variants/{variantId}` - Eliminar variante

### Actualizaciones

- **ProductDto**: Agregados campos para imágenes, variantes y modelo 3D principal
- **application.properties**: Aumentado límite de upload a 50MB

## 📊 Base de Datos

### Nueva Tabla: `product_variants`
```sql
CREATE TABLE product_variants (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100),
    color VARCHAR(50),
    size VARCHAR(50),
    material VARCHAR(100),
    price_modifier DECIMAL(10,2),
    stock INT,
    image_url VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### Nueva Tabla: `product_images`
```sql
CREATE TABLE product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    display_order INT DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### Nueva Tabla: `product_models_3d`
```sql
CREATE TABLE product_models_3d (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    url VARCHAR(500) NOT NULL,
    format VARCHAR(10),
    file_size BIGINT,
    thumbnail_url VARCHAR(500),
    optimized BOOLEAN DEFAULT FALSE,
    is_primary BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## 🔐 Seguridad

- Solo SELLER o ADMIN pueden subir/eliminar imágenes y modelos
- Validación de tipos de archivo
- Límites de tamaño: 10MB imágenes, 50MB modelos 3D
- Validación de permisos por producto

## 📝 Ejemplos de Uso

### Subir Imagen
```bash
POST /api/products/1/images
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [binary]
isPrimary: true
displayOrder: 0
```

### Subir Modelo 3D
```bash
POST /api/products/1/models3d
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [GLB/GLTF binary]
isPrimary: true
```

### Crear Variante
```bash
POST /api/products/1/variants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Rojo - Talla M",
  "sku": "PROD-001-RED-M",
  "color": "Rojo",
  "size": "M",
  "priceModifier": 0.00,
  "stock": 50,
  "active": true
}
```

## ✅ Checklist

- [x] Modelos creados (Variant, Image, Model3D)
- [x] Repositorios creados
- [x] FileStorageService implementado
- [x] Controllers REST creados
- [x] Validaciones de archivos
- [x] DTOs actualizados
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Optimización DRACO (pendiente)
- [ ] Generación real de thumbnails (pendiente)
- [ ] Integración S3/MinIO (pendiente)

## 🚀 Comandos para Probar

```bash
# Subir imagen
curl -X POST http://localhost:8081/api/products/1/images \
  -H "Authorization: Bearer {token}" \
  -F "file=@image.jpg" \
  -F "isPrimary=true"

# Listar imágenes
curl http://localhost:8081/api/products/1/images

# Subir modelo 3D
curl -X POST http://localhost:8081/api/products/1/models3d \
  -H "Authorization: Bearer {token}" \
  -F "file=@model.glb"

# Crear variante
curl -X POST http://localhost:8081/api/products/1/variants \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Azul - Talla L",
    "color": "Azul",
    "size": "L",
    "stock": 30
  }'
```

## 📌 Notas

- Los archivos se almacenan localmente en `./uploads/products/`
- La generación de thumbnails es un placeholder (retorna misma URL)
- La optimización DRACO está marcada como pendiente
- Para producción, integrar S3/MinIO para almacenamiento cloud
- Los modelos 3D se validan por extensión y content-type

## 🔗 Issues Relacionados

- #3 - Variantes de productos
- #4 - Upload de modelos 3D
- #5 - Múltiples imágenes

---

**Labels:** `feature`, `backend`, `media`, `3d`
**Tiempo estimado:** 6-8 horas
**Prioridad:** Alta

