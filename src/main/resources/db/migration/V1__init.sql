CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users
CREATE TABLE app_users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Catalogs
CREATE TABLE catalogs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cover_image VARCHAR(500),
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Antiques
CREATE TABLE antiques (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    catalog_id UUID REFERENCES catalogs(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    subcategory VARCHAR(255) NOT NULL,
    detail VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    element VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    author VARCHAR(255),
    editor VARCHAR(255),
    imprenta VARCHAR(255),
    edition VARCHAR(255),
    signature VARCHAR(255),
    theme VARCHAR(255),
    century VARCHAR(50),
    description TEXT,
    price DECIMAL(10,2) NOT NULL DEFAULT 0,
    year_era VARCHAR(50),
    condition VARCHAR(50) NOT NULL,
    material VARCHAR(255),
    dimensions VARCHAR(255),
    paper_type VARCHAR(100),
    paper_format VARCHAR(100),
    paper_weight INT,
    images TEXT[] DEFAULT ARRAY[]::TEXT[],
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Antique images
CREATE TABLE antique_images (
    id BIGSERIAL PRIMARY KEY,
    antique_id UUID NOT NULL REFERENCES antiques(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL
);

-- Seed admin user
INSERT INTO app_users (email, password, role, name) VALUES
    ('admin@antiguedades.com', 'admin123', 'admin', 'Administrador');
