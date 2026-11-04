-- Optional explicit migration for environments that do not use Hibernate ddl-auto=update.
-- Existing companies retain the established face-verification attendance behavior.
ALTER TABLE companies
    ADD COLUMN IF NOT EXISTS face_verification_attendance boolean NOT NULL DEFAULT false;
ALTER TABLE companies ALTER COLUMN face_verification_attendance SET DEFAULT false;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS task_enabled boolean NOT NULL DEFAULT false;
