DO $$
BEGIN

  -- Client Industry Type ==========================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE table_name = 'client_industry_type')
  THEN
    CREATE TABLE client_industry_type (
      id          UUID         NOT NULL,
      l10n_key    VARCHAR(255) NOT NULL,
      enabled     BOOLEAN      NOT NULL DEFAULT FALSE,
      order_index INT4         NOT NULL,
      PRIMARY KEY (id)
    );
    INSERT INTO client_industry_type (id, l10n_key, enabled, order_index)
    VALUES ('bb0c2075-cc2f-4419-be55-86317f1359b7', 'Banque / Assurance', TRUE, 0);
    INSERT INTO client_industry_type (id, l10n_key, enabled, order_index)
    VALUES ('a366ec02-6cd1-4b52-b06c-5c0a2870b7b1', 'Transport / Energie', TRUE, 1);
    INSERT INTO client_industry_type (id, l10n_key, enabled, order_index)
    VALUES ('c78f89d1-cb8c-4d67-a59e-e72c23f1cd38', 'Retail / Distribution / Luxe', TRUE, 2);
    INSERT INTO client_industry_type (id, l10n_key, enabled, order_index)
    VALUES ('bf8ccf16-9a13-4f8f-9017-b0c3f148d331', 'Secteur public', TRUE, 3);
    INSERT INTO client_industry_type (id, l10n_key, enabled, order_index)
    VALUES ('d78ad9e4-e46a-43d8-aa88-a535911ebc15', 'Santé / Chimie / Pharmacie', TRUE, 4);

    CREATE TABLE conditions_client_industry_types (
      conditions_talent_id     UUID NOT NULL,
      client_industry_types_id UUID NOT NULL,
      PRIMARY KEY (conditions_talent_id, client_industry_types_id)
    );
    ALTER TABLE conditions_client_industry_types
      ADD CONSTRAINT FK_conditions_client_industry_types__client_industry_type FOREIGN KEY (client_industry_types_id) REFERENCES client_industry_type;
    ALTER TABLE conditions_client_industry_types
      ADD CONSTRAINT FK_conditions_client_industry_types__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
  END IF;

  -- Conditions ====================================================================================

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'conditions' AND column_name = 'internship')
  THEN
    ALTER TABLE conditions
      ADD internship BOOLEAN NOT NULL DEFAULT FALSE;
  END IF;

END$$;