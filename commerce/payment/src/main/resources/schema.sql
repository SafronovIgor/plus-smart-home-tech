DROP TABLE if exists payments CASCADE;

CREATE TABLE IF NOT EXISTS payments
(
    payment_id     uuid DEFAULT gen_random_uuid(),
    order_id       uuid    NOT NULL UNIQUE,
    total_payment  numeric NOT NULL,
    delivery_total numeric NOT NULL,
    fee_total      numeric NOT NULL,
    payment_state  varchar NOT NULL,

    CONSTRAINT PK_deliveries PRIMARY KEY (payment_id)
);