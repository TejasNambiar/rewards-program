-- Create Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount DOUBLE PRECISION NOT NULL, -- Changes to double precision floating-point
    transaction_date DATE NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);