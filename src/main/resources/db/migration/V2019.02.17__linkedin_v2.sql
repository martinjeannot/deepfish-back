DO $$
BEGIN

  -- Talent ========================================================================================

  ALTER TABLE talent
    ALTER COLUMN basic_profile_text TYPE JSON USING basic_profile_text :: JSON;

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'linkedin_id')
  THEN
    ALTER TABLE talent
      RENAME COLUMN linked_in_id TO linkedin_id;
  END IF;

END$$;