DROP TABLE if exists deliveries CASCADE;
DROP TABLE if exists addresses CASCADE;

CREATE TABLE IF NOT EXISTS deliveries
(
    delivery_id     uuid DEFAULT gen_random_uuid(),
    from_address_id uuid NOT NULL,
    to_address_id   uuid NOT NULL,
    order_id        uuid NOT NULL,
    deliveryState   varchar(50),

    CONSTRAINT PK_deliveries PRIMARY KEY (delivery_id),
    CONSTRAINT FK_deliveries_addresses_from_address_id FOREIGN KEY (from_address_id) REFERENCES addresses (address_id),
    CONSTRAINT FK_deliveries_addresses_to_address_id FOREIGN KEY (to_address_id) REFERENCES addresses (address_id)
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
)