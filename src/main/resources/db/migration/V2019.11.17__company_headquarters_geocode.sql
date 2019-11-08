DO $$
BEGIN

  -- Add headquarters geocode

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'company' AND column_name = 'headquarters_geocode')
  THEN
    ALTER TABLE company
      ADD headquarters_geocode JSONB;
  END IF;

END$$;