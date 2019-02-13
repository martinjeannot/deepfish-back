DO $$
BEGIN

  -- Talent ========================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'linkedin_in')
  THEN
    ALTER TABLE talent
      RENAME COLUMN linked_in_id TO linkedin_id;
  END IF;

END$$;