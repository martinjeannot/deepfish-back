DO $$
BEGIN

  -- [Talent] add talent advocate

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'talent_advocate_id')
  THEN
    ALTER TABLE talent
      ADD talent_advocate_id UUID;
    ALTER TABLE talent
      ADD CONSTRAINT FK_talent__users__talent_advocate_id FOREIGN KEY (talent_advocate_id) REFERENCES users;
  END IF;

END$$;
