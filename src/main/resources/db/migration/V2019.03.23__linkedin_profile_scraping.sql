DO $$
BEGIN

  -- Talent ========================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'talent' AND column_name = 'linkedin_profile_last_retrieved_at')
  THEN
    ALTER TABLE talent
      ADD linkedin_profile_last_retrieved_at TIMESTAMP,
      ADD linkedin_profile_last_retrieval_attempted_at TIMESTAMP;
  END IF;

END$$;