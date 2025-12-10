# ⚠️ VALIDACIÓN EXTINCIÓN - Controllers Thymeleaf

## 🗑️ ARCHIVOS A ELIMINAR

### Controllers Thymeleaf (18 archivos)
Estos controllers retornan vistas HTML y ya NO son necesarios:

1. `AdminBrandController.java`
2. `AdminCategoryController.java`
3. `AdminController.java`
4. `AdminOrderController.java`
5. `AdminProductController.java`
6. `AuthController.java`
7. `BrandController.java`
8. `CartController.java`
9. `CategoryController.java`
10. `DashboardController.java`
11. `HomeController.java`
12. `OrderController.java`
13. `PasswordController.java`
14. `PaymentController.java`
15. `ProductController.java`
16. `ProfileController.java`
17. `RegistrationController.java`
18. `ReportController.java`

### Templates HTML (50+ archivos)
Toda la carpeta `src/main/resources/templates/` debe eliminarse.

### Dependencias
- `spring-boot-starter-thymeleaf` del pom.xml (ya removida)
- `thymeleaf-extras-springsecurity6` del pom.xml (ya removida)

## ✅ REEMPLAZO

Todas las funcionalidades están disponibles en:
- APIs REST en `/api/**`
- Frontend Next.js completo

## 🔄 ACCIÓN

**Procediendo con la eliminación...**

