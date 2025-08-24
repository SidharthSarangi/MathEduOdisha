CREATE TABLE cart_items (
    cart_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    price NUMERIC(10,2) NOT NULL
);
