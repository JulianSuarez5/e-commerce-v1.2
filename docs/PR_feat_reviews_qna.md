# PR: feat/reviews-qna

## 📋 Descripción

Implementación completa de sistema de Reviews (calificaciones) y Q&A (Preguntas y Respuestas) estilo MercadoLibre.

## 🎯 Cambios Realizados

### Nuevos Modelos

#### Review
- Calificaciones de 1-5 estrellas
- Comentarios y títulos
- Verificación de compra
- Sistema de "útil" (helpful)
- Una review por usuario por producto

#### Question
- Preguntas sobre productos
- Estado de respuesta
- Relación con producto y usuario

#### Answer
- Respuestas a preguntas
- Pueden ser del vendedor o de otros usuarios
- Marca de respuesta oficial del vendedor

#### OrderTracking
- Historial de estados de orden
- Tracking number
- Ubicación y descripción
- Timestamps

### Nuevos Repositorios
- ReviewRepository
- QuestionRepository
- AnswerRepository
- OrderTrackingRepository

### Nuevos Servicios
- ReviewService / ReviewServiceImpl
- QuestionService / QuestionServiceImpl

### Nuevos Controllers

#### ReviewApiController
- `POST /api/products/{id}/reviews` - Crear review
- `GET /api/products/{id}/reviews` - Listar reviews
- `GET /api/products/{id}/reviews/stats` - Estadísticas (rating promedio, total)

#### QuestionApiController
- `POST /api/products/{id}/questions` - Crear pregunta
- `GET /api/products/{id}/questions` - Listar preguntas
- `POST /api/products/{id}/questions/{questionId}/answers` - Responder pregunta

#### OrderApiController
- `GET /api/orders` - Listar órdenes del usuario
- `GET /api/orders/{id}` - Detalle de orden
- `GET /api/orders/{id}/tracking` - Historial de tracking
- `PUT /api/orders/{id}/status` - Actualizar estado (admin)

## 📊 Base de Datos

### Nueva Tabla: `reviews`
```sql
CREATE TABLE reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    title VARCHAR(200),
    comment VARCHAR(1000),
    verified_purchase BOOLEAN DEFAULT FALSE,
    helpful BOOLEAN DEFAULT FALSE,
    helpful_count INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_user_product (user_id, product_id)
);
```

### Nueva Tabla: `questions`
```sql
CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    question VARCHAR(500) NOT NULL,
    answered BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Nueva Tabla: `answers`
```sql
CREATE TABLE answers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT NOT NULL,
    user_id INT NOT NULL,
    answer VARCHAR(1000) NOT NULL,
    is_seller_answer BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Nueva Tabla: `order_tracking`
```sql
CREATE TABLE order_tracking (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    location VARCHAR(255),
    tracking_number VARCHAR(100),
    timestamp TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
```

## 🔐 Seguridad

- Solo usuarios autenticados pueden crear reviews y preguntas
- Un usuario solo puede hacer una review por producto
- Solo el vendedor o admin pueden responder preguntas como "seller answer"
- Solo admin puede actualizar estados de órdenes

## 📝 Ejemplos de Uso

### Crear Review
```bash
POST /api/products/1/reviews
Authorization: Bearer {token}
Content-Type: application/json

{
  "rating": 5,
  "title": "Excelente producto",
  "comment": "Muy satisfecho con la compra"
}
```

### Crear Pregunta
```bash
POST /api/products/1/questions
Authorization: Bearer {token}
Content-Type: application/json

{
  "question": "¿Cuál es el tiempo de envío?"
}
```

### Responder Pregunta
```bash
POST /api/products/1/questions/1/answers
Authorization: Bearer {token}
Content-Type: application/json

{
  "answer": "El envío tarda entre 3-5 días hábiles",
  "isSeller": true
}
```

### Ver Tracking de Orden
```bash
GET /api/orders/1/tracking
Authorization: Bearer {token}
```

## ✅ Checklist

- [x] Modelos creados (Review, Question, Answer, OrderTracking)
- [x] Repositorios creados
- [x] Servicios implementados
- [x] Controllers REST creados
- [x] Validaciones implementadas
- [x] DTOs creados
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] UI Frontend para reviews y Q&A

## 🚀 Comandos para Probar

```bash
# Crear review
curl -X POST http://localhost:8081/api/products/1/reviews \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5,
    "title": "Excelente",
    "comment": "Muy bueno"
  }'

# Ver reviews
curl http://localhost:8081/api/products/1/reviews

# Ver stats
curl http://localhost:8081/api/products/1/reviews/stats

# Crear pregunta
curl -X POST http://localhost:8081/api/products/1/questions \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "¿Tiene garantía?"
  }'
```

## 📌 Notas

- Un usuario solo puede hacer una review por producto
- Las preguntas pueden tener múltiples respuestas
- El sistema marca automáticamente si una respuesta es del vendedor
- El tracking de órdenes se crea automáticamente al cambiar estados
- Las estadísticas de reviews se calculan en tiempo real

## 🔗 Issues Relacionados

- #9 - Sistema de reviews
- #10 - Preguntas y respuestas
- #11 - Tracking de órdenes

---

**Labels:** `feature`, `backend`, `reviews`, `qna`
**Tiempo estimado:** 4-6 horas
**Prioridad:** Media

