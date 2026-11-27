-- Scalable Website Module Schema (com.hr.demo.website)
-- Indexed for O(log N) lookups and high-throughput read/write separation

CREATE TABLE IF NOT EXISTS website_contact_inquiries (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(160) NOT NULL,
    company VARCHAR(160),
    phone VARCHAR(40),
    service_interest VARCHAR(120) NOT NULL,
    selected_plan VARCHAR(80),
    message TEXT NOT NULL,
    source_page VARCHAR(80) DEFAULT 'contact',
    status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_website_inquiries_status_created
    ON website_contact_inquiries (status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_website_inquiries_email
    ON website_contact_inquiries (email);

CREATE TABLE IF NOT EXISTS website_jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(160) NOT NULL,
    department VARCHAR(100) NOT NULL,
    location VARCHAR(120) NOT NULL,
    employment_type VARCHAR(60) NOT NULL,
    salary_range VARCHAR(80) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_website_jobs_active_created
    ON website_jobs (active, created_at DESC);

CREATE TABLE IF NOT EXISTS website_job_applications (
    id BIGSERIAL PRIMARY KEY,
    job_id BIGINT REFERENCES website_jobs(id) ON DELETE SET NULL,
    job_title VARCHAR(160) NOT NULL,
    applicant_name VARCHAR(160) NOT NULL,
    email VARCHAR(160) NOT NULL,
    phone VARCHAR(40),
    linkedin_url VARCHAR(255),
    cover_note TEXT,
    resume_path VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'RECEIVED',
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_website_apps_status_applied
    ON website_job_applications (status, applied_at DESC);
CREATE INDEX IF NOT EXISTS idx_website_apps_email
    ON website_job_applications (email);

CREATE TABLE IF NOT EXISTS website_newsletter_subscribers (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(160) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    subscribed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS website_services (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(80) NOT NULL,
    title VARCHAR(140) NOT NULL,
    subtitle VARCHAR(180),
    description TEXT NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_website_services_active_order
    ON website_services (active, sort_order ASC);

CREATE TABLE IF NOT EXISTS website_pricing_plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price_label VARCHAR(40) NOT NULL,
    period_label VARCHAR(40) NOT NULL,
    popular BOOLEAN NOT NULL DEFAULT FALSE,
    features_csv TEXT NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS website_blog_posts (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(160) NOT NULL UNIQUE,
    category VARCHAR(80) NOT NULL,
    title VARCHAR(200) NOT NULL,
    excerpt TEXT NOT NULL,
    content TEXT,
    image_url VARCHAR(255),
    published_date VARCHAR(60) NOT NULL,
    author VARCHAR(100) NOT NULL,
    read_time VARCHAR(40) NOT NULL,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_website_blogs_active_created
    ON website_blog_posts (active, created_at DESC);
