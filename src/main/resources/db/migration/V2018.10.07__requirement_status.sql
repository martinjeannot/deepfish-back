-- Requirement : add status field ==================================================================

ALTER TABLE requirement
  ADD status VARCHAR(255);

UPDATE requirement
SET status = 'OPEN';

ALTER TABLE requirement
  ALTER COLUMN status SET NOT NULL;