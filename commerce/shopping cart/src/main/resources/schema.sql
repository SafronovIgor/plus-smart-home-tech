DROP TABLE if exists carts_products CASCADE;
DROP TABLE if exists shopping_carts CASCADE;

CREATE TABLE IF NOT EXISTS shopping_carts
(
    shopping_cart_id uuid DEFAULT gen_random_uuid(),
    username         VARCHAR NOT NULL,
    activated        BOOLEAN NOT NULL,
    CONSTRAINT shopping_carts_PK PRIMARY KEY (shopping_cart_id)
);


CREATE TABLE IF NOT EXISTS carts_products
(
    shopping_cart_id uuid   NOT NULL,
    product_id       uuid   NOT NULL,
    quantity         BIGINT NOT NULL,
    CONSTRAINT carts_products_PK PRIMARY KEY (shopping_cart_id, product_id),
    CONSTRAINT FK_SHOPPING_CART_CARTS_PRODUCTS FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts (shopping_cart_id) ON DELETE CASCADE
);