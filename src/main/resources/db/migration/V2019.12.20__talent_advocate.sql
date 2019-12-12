DO $$
BEGIN

  -- [Talent]

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'talent_advocate_id')
  THEN
    ALTER TABLE talent
      ADD talent_advocate_id UUID,
      ADD online BOOLEAN NOT NULL DEFAULT FALSE,
      ADD onlined_at TIMESTAMP,
      ADD job_function VARCHAR(255) NOT NULL DEFAULT 'SALES';
    ALTER TABLE talent
      ADD CONSTRAINT FK_talent__users__talent_advocate_id FOREIGN KEY (talent_advocate_id) REFERENCES users;
  END IF;


  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE
                  table_name = 'conditions_company_blacklist')
  THEN
    CREATE TABLE conditions_company_blacklist (
      conditions_talent_id UUID NOT NULL,
      company_blacklist_id UUID NOT NULL,
      PRIMARY KEY (conditions_talent_id, company_blacklist_id)
    );
    ALTER TABLE conditions_company_blacklist
      ADD CONSTRAINT FK_conditions_company_blacklist__company FOREIGN KEY (company_blacklist_id) REFERENCES company;
    ALTER TABLE conditions_company_blacklist
      ADD CONSTRAINT FK_conditions_company_blacklist__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
  END IF;

  -- [Employer]

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

  -- [Company]

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'company' AND column_name = 'status')
  THEN
    ALTER TABLE company
      ADD status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
      ADD validated_at TIMESTAMP;
  END IF;

  -- [Requirement]

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'requirement' AND column_name = 'version')
  THEN
    ALTER TABLE requirement
      ADD version INT4 NOT NULL DEFAULT 1,
      ADD job_function VARCHAR(255) NOT NULL DEFAULT 'SALES';
    ALTER TABLE requirement
      ALTER COLUMN version SET DEFAULT 2;
  END IF;

  -- [Opportunity]

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'opportunity' AND column_name = 'version')
  THEN
    ALTER TABLE opportunity
      ADD version INT4 NOT NULL DEFAULT 1,
      ADD name VARCHAR(255) NOT NULL DEFAULT '',
      ADD base_salary_from REAL,
      ADD base_salary_to REAL,
      ADD employer_id UUID,
      ALTER COLUMN creator_id DROP NOT NULL;
    ALTER TABLE opportunity
      ALTER COLUMN version SET DEFAULT 2,
      ADD CONSTRAINT FK_opportunity__employer__employer_id FOREIGN KEY (employer_id) REFERENCES employer;
  END IF;

END$$;
