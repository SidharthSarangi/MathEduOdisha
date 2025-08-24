CREATE TABLE magazines (
    id UUID PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author_id UUID NOT NULL,
    publisher VARCHAR(255),
    issn VARCHAR(50),
    published_date DATE,
    price NUMERIC(10,2) NOT NULL,
    file_url TEXT NOT NULL,
    cover_image_url TEXT NOT NULL,
    description TEXT,
    page_count INT,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    product_type VARCHAR(255) NOT NULL DEFAULT 'Magazine'
);