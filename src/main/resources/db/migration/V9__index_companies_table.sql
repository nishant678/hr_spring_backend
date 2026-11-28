CREATE INDEX IF NOT EXISTS idx_companies_status_created
    ON companies (status, created_at DESC);
