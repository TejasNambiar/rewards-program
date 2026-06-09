INSERT INTO public.customers (id, name) VALUES (1, 'John Doe') ON CONFLICT DO NOTHING;
INSERT INTO public.customers (id, name) VALUES (2, 'Jane Smith') ON CONFLICT DO NOTHING;

-- Jan 2026
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (1, 120.00, '2026-01-10');
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (1, 75.00, '2026-01-22');
-- Feb 2026
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (1, 200.00, '2026-02-14');
-- Mar 2026
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (1, 50.00, '2026-03-05');

-- Customer 2
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (2, 80.00, '2026-01-11');
INSERT INTO public.transactions (customer_id, amount, transaction_date) VALUES (2, 110.00, '2026-02-20');