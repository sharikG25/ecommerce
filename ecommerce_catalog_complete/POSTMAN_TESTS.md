# Pruebas en Postman

## Configuración

Auth:

`http://localhost:8080`

Catalog:

`http://localhost:8081`

Usa `Content-Type: application/json`.

## 1. Crear usuario

`POST http://localhost:8080/api/ecommerce/users`

```json
{
  "name": "Test User",
  "mail": "test@ecommerce.com",
  "password": "123456",
  "role": "CUSTOMER",
  "age": 25,
  "phoneNumber": "3001234567"
}
```

Guarda el valor `idUser`.

## 2. Login

`POST http://localhost:8080/api/ecommerce/users/login`

```json
{
  "mail": "test@ecommerce.com",
  "password": "123456"
}
```

## 3. Consultar saldo inicial

`GET http://localhost:8080/api/ecommerce/users/{userId}/balance`

Debe iniciar en `0.00`.

## 4. Recargar saldo

`POST http://localhost:8080/api/ecommerce/users/{userId}/balance/recharge`

```json
{
  "amount": 250000
}
```

## 5. Crear producto

`POST http://localhost:8081/api/ecommerce/products`

```json
{
  "name": "Mouse inalámbrico",
  "description": "Mouse para computador",
  "price": 45000,
  "stock": 10,
  "imageUrl": "https://example.com/mouse.jpg"
}
```

Guarda el valor `id`.

## 6. Consultar y listar productos

`GET http://localhost:8081/api/ecommerce/products/{productId}`

`GET http://localhost:8081/api/ecommerce/products`

## 7. Crear u obtener carrito activo

`POST http://localhost:8081/api/ecommerce/carts/users/{userId}`

Guarda el valor `id` del carrito.

## 8. Agregar producto

`POST http://localhost:8081/api/ecommerce/carts/{cartId}/items`

```json
{
  "productId": 1,
  "quantity": 2
}
```

Reemplaza `productId` por el identificador real.

## 9. Agregar el mismo producto otra vez

Repite el request anterior. El carrito debe conservar un solo ítem e incrementar la cantidad.

## 10. Cambiar cantidad

`PUT http://localhost:8081/api/ecommerce/carts/{cartId}/items/{productId}`

```json
{
  "quantity": 2
}
```

## 11. Consultar carrito

`GET http://localhost:8081/api/ecommerce/carts/{cartId}`

Verifica `unitPrice`, `subtotal` y `total`.

## 12. Ejecutar compra

`POST http://localhost:8081/api/ecommerce/carts/{cartId}/checkout`

Verifica que el estado quede `PURCHASED`.

## 13. Verificar saldo final

`GET http://localhost:8080/api/ecommerce/users/{userId}/balance`

## 14. Verificar stock final

`GET http://localhost:8081/api/ecommerce/products/{productId}`

## 15. Probar cobro duplicado

Repite:

`POST http://localhost:8081/api/ecommerce/carts/{cartId}/checkout`

Debe responder:

```json
{
  "message": "Esta compra ya fue procesada."
}
```

## 16. Probar precio inválido

`POST http://localhost:8081/api/ecommerce/products`

```json
{
  "name": "Producto inválido",
  "description": "Prueba",
  "price": 0,
  "stock": 10,
  "imageUrl": ""
}
```

Debe rechazarlo con `400 Bad Request`.

## 17. Probar stock negativo

Envía un producto con:

```json
{
  "stock": -1
}
```

Debe rechazarlo con `400 Bad Request`.

## 18. Probar cantidad inválida

Agrega o actualiza un ítem con:

```json
{
  "quantity": 0
}
```

Debe rechazarlo con `400 Bad Request`.

## 19. Probar saldo insuficiente

Crea un nuevo carrito activo y agrega productos cuyo total supere el saldo. Al ejecutar checkout debe responder conflicto y no modificar ni el stock ni el saldo.

## 20. Probar stock insuficiente

Crea un carrito con una cantidad válida, luego reduce el stock del producto mediante el endpoint de actualización antes del checkout. El checkout debe volver a validar el inventario y rechazar toda la compra sin descontar saldo.
