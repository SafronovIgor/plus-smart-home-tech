DROP TABLE if exists orders CASCADE;
DROP TABLE if exists addresses CASCADE;

CREATE TABLE IF NOT EXISTS orders
(
    order_id         uuid DEFAULT gen_random_uuid(),
    username         varchar NOT NULL,
    shopping_cart_id uuid    NOT NULL,
    payment_id       uuid,
    delivery_id      uuid,
    delivery_volume  double precision,
    delivery_weight  double precision,
    fragile          boolean,
    total_price      double precision,
    delivery_price   double precision,
    product_price    double precision,
    state            varchar(50),
    address_id       uuid,

    CONSTRAINT orders_PK PRIMARY KEY (order_id),
    CONSTRAINT FK_orders_addresses_address_id FOREIGN KEY (address_id) REFERENCES addresses (address_id)

);

CREATE TABLE IF NOT EXISTS addresses
(
    address_id uuid DEFAULT gen_random_uuid(),
    country    varchar(50),
    city       varchar(50),
    street     varchar(50),
    house      varchar(50),
    flat       varchar(50),

    CONSTRAINT PK_addresses PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS orders_products
(
    order_id   uuid,
    product_id uuid,
    quantity   bigint,

    CONSTRAINT PK_orders_products PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_orders_products_orders FOREIGN KEY (order_id) REFERENCES orders (order_id)
)