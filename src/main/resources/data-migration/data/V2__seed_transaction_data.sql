-- 1. Seed 4 Distinct Customers
INSERT INTO customers (id, name) VALUES (1, 'Alice Smith');
INSERT INTO customers (id, name) VALUES (2, 'Bob Jones');
INSERT INTO customers (id, name) VALUES (3, 'Charlie Brown');
INSERT INTO customers (id, name) VALUES (4, 'Diana Prince');

-- Reset identity sequence values for auto-incrementing IDs after manual inserts
ALTER SEQUENCE customers_id_seq RESTART WITH 5;

-- 2. Seed 6 Months of Transaction History (Trailing back from June 2026)

-- ======== DEC 2025 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 120.00, '2025-12-05'); -- 90 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 75.00, '2025-12-12');  -- 25 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 40.00, '2025-12-20');  -- 0 pts

-- ======== JAN 2026 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 55.00, '2026-01-10');  -- 5 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 210.00, '2026-01-15'); -- 270 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (4, 145.00, '2026-01-28'); -- 140 pts

-- ======== FEB 2026 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 100.00, '2026-02-02'); -- 50 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 115.00, '2026-02-14'); -- 80 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (4, 85.00, '2026-02-22');  -- 35 pts

-- ======== MAR 2026 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 60.00, '2026-03-03');  -- 10 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 180.00, '2026-03-19'); -- 210 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 125.00, '2026-03-25'); -- 100 pts

-- ======== APR 2026 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 95.00, '2026-04-05');  -- 45 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (4, 50.00, '2026-04-18');  -- 0 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 130.00, '2026-04-30'); -- 110 pts

-- ======== MAY 2026 ========
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 70.00, '2026-05-04');  -- 20 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (4, 250.00, '2026-05-15'); -- 350 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 105.00, '2026-05-29'); -- 60 pts