DO $$
BEGIN

  -- UTM ===========================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE table_name = 'utm')
  THEN
    CREATE TABLE utm (
      id         UUID         NOT NULL,
      created_at TIMESTAMP    NOT NULL,
      source     VARCHAR(255) NOT NULL,
      medium     VARCHAR(255),
      campaign   VARCHAR(255),
      term       VARCHAR(255),
      content    VARCHAR(255),
      PRIMARY KEY (id)
    );

    -- TALENT
    ALTER TABLE talent
      ADD utm_id UUID;
    ALTER TABLE talent
      ADD CONSTRAINT FK_talent__utm__utm_id FOREIGN KEY (utm_id) REFERENCES utm;
  END IF;

END$$;