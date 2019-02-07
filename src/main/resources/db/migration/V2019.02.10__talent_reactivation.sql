DO $$
BEGIN

  -- Talent ========================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'reactivated_on')
  THEN
    ALTER TABLE talent
      ADD reactivated_on DATE;
  END IF;

END$$;