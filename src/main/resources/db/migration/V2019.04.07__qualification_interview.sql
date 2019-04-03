DO $$
BEGIN

  -- Qualification =================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'qualification' AND column_name = 'interview_scheduled')
  THEN
    ALTER TABLE qualification
      ADD interview_scheduled BOOLEAN NOT NULL DEFAULT FALSE;
  END IF;

END$$;