CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    payment_uuid UUID NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    product_id UUID NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,            -- CREATED, SUCCESS, FAILED
    razorpay_order_id VARCHAR(100) NOT NULL UNIQUE,
    razorpay_payment_id VARCHAR(100),
    razorpay_signature VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_user_product ON payments(user_id, product_id);
CREATE INDEX idx_payments_status ON payments(status);

-- trigger to update updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_updated_at
BEFORE UPDATE ON payments
FOR EACH ROW EXECUTE FUNCTION set_updated_at();
