DO $$
BEGIN

  -- Opportunity : improved event monitoring =======================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'opportunity' AND column_name = 'employer_accepted_at')
  THEN
    ALTER TABLE opportunity
      ADD employer_accepted_at TIMESTAMP,
      ADD employer_declined_at TIMESTAMP,
      ADD talent_started_on DATE,
      ADD trial_period_terminated_on DATE;
  END IF;

END$$;