DO $$
BEGIN

  -- Add new task type for Talent Acquisition

  IF NOT EXISTS(SELECT 1
                FROM task_type
                WHERE id = '6bb4d843-5ce7-4969-afcf-20129d48bb1e')
  THEN
    INSERT INTO task_type (id, l10n_key, order_index, enabled)
    VALUES ('6bb4d843-5ce7-4969-afcf-20129d48bb1e', 'Sourcing de candidats', 4, TRUE);
  END IF;

  -- Add trial period termination reason

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'opportunity' AND column_name = 'trial_period_termination_reason')
  THEN
    ALTER TABLE opportunity
      ADD trial_period_termination_reason TEXT;
  END IF;

  -- Add seen by talent at to opportunity

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'opportunity' AND column_name = 'seen_by_talent_at')
  THEN
    ALTER TABLE opportunity
      ADD seen_by_talent_at TIMESTAMP;
  END IF;

END$$;
