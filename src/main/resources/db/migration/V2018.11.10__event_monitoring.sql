-- Opportunity : add event tracking fields =========================================================

ALTER TABLE opportunity
  ADD talent_responded_at TIMESTAMP,
  ADD employer_responded_at TIMESTAMP,
  ADD forwarded_once BOOLEAN;

-- Default values for talent and employer response

UPDATE opportunity
SET talent_responded_at = created_at
WHERE talent_status != 'PENDING';

UPDATE opportunity
SET employer_responded_at = forwarded_at
WHERE employer_status IS NOT NULL AND employer_status != 'PENDING';

-- Default values for forwardedOnce

UPDATE opportunity
SET forwarded_once = FALSE;

UPDATE opportunity
SET forwarded_once = TRUE
WHERE employer_status IS NULL AND forwarded_at IS NOT NULL;

ALTER TABLE opportunity
  ALTER COLUMN forwarded_once SET NOT NULL;

-- ForwardedAt cleanup

UPDATE opportunity
SET forwarded_at = NULL
WHERE employer_status IS NULL AND forwarded_at IS NOT NULL;