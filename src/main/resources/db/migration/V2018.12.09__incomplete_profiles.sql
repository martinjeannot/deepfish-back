DO $$
BEGIN

  -- Incomplete profiles related modifications =====================================================

  -- Update talent

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'profile_completeness_last_calculated_at')
  THEN
    ALTER TABLE talent
      ADD profile_completeness_last_calculated_at TIMESTAMP NOT NULL DEFAULT '2018-01-01 00:00:00.000',
      ADD profile_completeness_last_updated_at TIMESTAMP NOT NULL DEFAULT '2018-01-01 00:00:00.000';
  END IF;

  -- Update remaining fr mail addresses ============================================================

  UPDATE users
  SET username = 'david@deepfish.co'
  WHERE username = 'david@deepfish.fr';

  UPDATE users
  SET username = 'martin@deepfish.co'
  WHERE username = 'admin@deepfish.fr';

END$$;