-- Requirement : add typeform JSONB field and remove some constraints ==============================

ALTER TABLE requirement
  ADD typeform JSONB;
ALTER TABLE requirement
  ALTER COLUMN location DROP NOT NULL;
ALTER TABLE requirement
  ALTER COLUMN job_type_id DROP NOT NULL;
ALTER TABLE requirement
  ALTER COLUMN seniority_id DROP NOT NULL;