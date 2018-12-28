DO $$
BEGIN

  -- Interview =====================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE table_name = 'interview')
  THEN
    CREATE TABLE interview (
      id                       UUID         NOT NULL,
      shared_id                UUID         NOT NULL,
      status                   VARCHAR(255) NOT NULL,
      created_at               TIMESTAMP    NOT NULL,
      creator_id               UUID         NOT NULL,
      updated_at               TIMESTAMP    NOT NULL,
      opportunity_id           UUID         NOT NULL,
      summary                  VARCHAR(255) NOT NULL,
      description              TEXT         NOT NULL,
      location                 TEXT         NOT NULL,
      start_at                 TIMESTAMP    NOT NULL,
      end_at                   TIMESTAMP    NOT NULL,
      format                   VARCHAR(255) NOT NULL,
      talent_id                UUID         NOT NULL,
      talent_response_status   VARCHAR(255) NOT NULL,
      talent_responded_at      TIMESTAMP,
      employer_id              UUID         NOT NULL,
      employer_response_status VARCHAR(255) NOT NULL,
      employer_responded_at    TIMESTAMP,
      PRIMARY KEY (id)
    );
    -- Constraints
    ALTER TABLE interview
      ADD CONSTRAINT FK_interview__employer__employer_id FOREIGN KEY (employer_id) REFERENCES employer;
    ALTER TABLE interview
      ADD CONSTRAINT FK_interview__opportunity__opportunity_id FOREIGN KEY (opportunity_id) REFERENCES opportunity;
    ALTER TABLE interview
      ADD CONSTRAINT FK_interview__talent__talent_id FOREIGN KEY (talent_id) REFERENCES talent;
  END IF;

END$$;