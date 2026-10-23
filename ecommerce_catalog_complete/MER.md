# Modelo Entidad-Relación

```mermaid
erDiagram
    USER {
        string idUser PK
        string name
        string mail
        string password
        string role
        int age
        string phoneNumber
        decimal balance
    }

    PRODUCT {
        long id PK
        string name
        string description
        decimal price
        int stock
        string imageUrl
    }

    SHOPPING_CART {
        long id PK
        string userId
        string status
        decimal total
    }

    CART_ITEM {
        long id PK
        long cartId FK
        long productId FK
        decimal unitPrice
        int quantity
        decimal subtotal
    }

    SHOPPING_CART ||--o{ CART_ITEM : contains
    PRODUCT ||--o{ CART_ITEM : references
```

`USER` pertenece al microservicio Auth y `PRODUCT`, `SHOPPING_CART` y `CART_ITEM` pertenecen al microservicio Catalog.

`SHOPPING_CART.userId` es una referencia lógica al identificador de Auth. No existe una clave foránea física entre las bases de datos de Auth y Catalog.

Dentro de Catalog, `SHOPPING_CART` se relaciona uno a muchos con `CART_ITEM`, y cada `CART_ITEM` referencia un `PRODUCT`.
