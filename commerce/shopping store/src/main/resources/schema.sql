DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE IF NOT EXISTS products
(
    product_id     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR,
    description    VARCHAR,
    image_src      VARCHAR,
    quantity_state VARCHAR,
    product_state  VARCHAR,
    rating         FLOAT,
    category       VARCHAR,
    price          FLOAT
);
