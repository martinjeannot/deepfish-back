DO $$
BEGIN

  -- Company =======================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'company' AND column_name = 'website_url')
  THEN
    ALTER TABLE company
      ADD website_url VARCHAR(255),
      ADD size VARCHAR(255),
      ADD headquarters_address VARCHAR(255),
      ADD founded_in VARCHAR(255),
      ADD revenue VARCHAR(255),
      ADD customer_references VARCHAR(255),
      ADD cover_image_uri VARCHAR(255),
      ADD top_image_uri VARCHAR(255),
      ADD bottom_image_uri VARCHAR(255),
      ADD facebook_url VARCHAR(255),
      ADD instagram_url VARCHAR(255),
      ADD linkedin_url VARCHAR(255),
      ADD twitter_url VARCHAR(255),
      ADD youtube_url VARCHAR(255);
  END IF;

END$$;