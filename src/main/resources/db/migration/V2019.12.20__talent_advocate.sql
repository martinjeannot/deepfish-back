DO $$
BEGIN

  -- [Talent] add talent advocate

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'talent_advocate_id')
  THEN
    ALTER TABLE talent
      ADD talent_advocate_id UUID,
      ADD online BOOLEAN NOT NULL DEFAULT FALSE,
      ADD onlined_at TIMESTAMP;
    ALTER TABLE talent
      ADD CONSTRAINT FK_talent__users__talent_advocate_id FOREIGN KEY (talent_advocate_id) REFERENCES users;
  END IF;

  -- [Employer] add client executive

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'employer' AND column_name = 'client_executive_id')
  THEN
    ALTER TABLE employer
      ADD client_executive_id UUID;
    ALTER TABLE employer
      ADD CONSTRAINT FK_employer__users__client_executive_id FOREIGN KEY (client_executive_id) REFERENCES users;
  END IF;

  -- [Company] add status

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'company' AND column_name = 'status')
  THEN
    ALTER TABLE company
      ADD status VARCHAR(255),
      ADD validated_at TIMESTAMP;
    UPDATE company
    SET status = 'PENDING';
    ALTER TABLE company
      ALTER COLUMN status SET NOT NULL;
  END IF;

END$$;
