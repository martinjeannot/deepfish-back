DO $$
BEGIN

  -- [Company] add headquarters geocode

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'company' AND column_name = 'headquarters_geocode')
  THEN
    ALTER TABLE company
      ADD headquarters_geocode JSONB;
  END IF;

  -- [Talent/Qualification] qualification / follow-up split

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'qualification' AND column_name = 'notes')
  THEN
    -- add notes field to qualification
    ALTER TABLE qualification
      ADD notes TEXT NOT NULL DEFAULT '';
    -- copy talent notes into qualification notes
    UPDATE qualification
    SET notes = (SELECT notes
                 FROM talent
                 WHERE talent.id = qualification.talent_id);
    -- rename talent notes
    ALTER TABLE talent
      RENAME notes TO follow_up;
  END IF;

END$$;