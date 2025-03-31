CREATE TABLE IF NOT EXISTS product(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);