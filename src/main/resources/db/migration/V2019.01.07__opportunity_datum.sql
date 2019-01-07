DO $$
BEGIN

  -- Opportunity datum =============================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE table_name = 'opportunity_datum')
  THEN
    CREATE TABLE opportunity_datum (
      created_at                  TIMESTAMP NOT NULL,
      total_created               INT8      NOT NULL,
      created_variation           INT8      NOT NULL,
      total_talent_pending        INT8      NOT NULL,
      talent_pending_variation    INT8      NOT NULL,
      total_employer_pending      INT8      NOT NULL,
      employer_pending_variation  INT8      NOT NULL,
      total_talent_accepted       INT8      NOT NULL,
      talent_accepted_variation   INT8      NOT NULL,
      total_employer_accepted     INT8      NOT NULL,
      employer_accepted_variation INT8      NOT NULL,
      total_talent_declined       INT8      NOT NULL,
      talent_declined_variation   INT8      NOT NULL,
      total_employer_declined     INT8      NOT NULL,
      employer_declined_variation INT8      NOT NULL,
      total_talent_expired        INT8      NOT NULL,
      talent_expired_variation    INT8      NOT NULL,
      PRIMARY KEY (created_at)
    );

    INSERT INTO opportunity_datum
    VALUES ('2019-01-01 00:00:00.000', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  END IF;

END$$;