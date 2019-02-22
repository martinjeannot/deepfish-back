DO $$
BEGIN

  -- Talent ========================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'linkedin_public_profile_url')
  THEN
    ALTER TABLE talent
      ADD linkedin_public_profile_url VARCHAR(255),
      ADD profile_picture_url VARCHAR(255) NOT NULL DEFAULT 'https://app.deepfish.co/static/img/avatar.png',
      ADD lite_profile_text JSON,
      ADD full_profile JSONB;
  END IF;

  -- Drop not null constraints
  ALTER TABLE talent
    ALTER COLUMN basic_profile DROP NOT NULL,
    ALTER COLUMN basic_profile_text DROP NOT NULL;

  -- Change basic_profile_text data type to JSON
  ALTER TABLE talent
    ALTER COLUMN basic_profile_text TYPE JSON USING basic_profile_text :: JSON;

  -- Rename linked_in_id column to linkedin_id
  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'linkedin_id')
  THEN
    ALTER TABLE talent
      RENAME COLUMN linked_in_id TO linkedin_id;
    ALTER TABLE talent
      RENAME CONSTRAINT uk_talent__linked_in_id TO uk_talent__linkedin_id;
  END IF;

END$$;