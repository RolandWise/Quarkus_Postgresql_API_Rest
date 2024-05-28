CREATE TABLE customer
(
    customer_id BIGSERIAL PRIMARY KEY,
    prenoms  TEXT NOT NULL,
    middle_name TEXT,
    nom   TEXT NOT NULL,
    suffix      TEXT,
    email       TEXT,
    phone_mobile       TEXT
);
ALTER SEQUENCE customer_customer_id_seq RESTART 1000;