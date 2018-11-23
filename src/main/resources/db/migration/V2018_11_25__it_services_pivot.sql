-- IT Services pivot related modifications =========================================================

DO $$
BEGIN

  -- Add new company maturity levels

  IF NOT EXISTS(SELECT 1
                FROM company_maturity_level
                WHERE id = 'b8e2b02f-0169-4d79-9fd5-1c298c9d5792')
  THEN
    INSERT INTO company_maturity_level (id, l10n_key, order_index)
    VALUES ('b8e2b02f-0169-4d79-9fd5-1c298c9d5792', 'Petite entreprise', 0);
  END IF;
  IF NOT EXISTS(SELECT 1
                FROM company_maturity_level
                WHERE id = 'f5173e2d-b558-4dbb-a2d4-5f3ca399aec3')
  THEN
    INSERT INTO company_maturity_level (id, l10n_key, order_index)
    VALUES ('f5173e2d-b558-4dbb-a2d4-5f3ca399aec3', 'PME', 1);
  END IF;
  IF NOT EXISTS(SELECT 1
                FROM company_maturity_level
                WHERE id = 'c2109cda-d100-4467-861c-d10b850df51e')
  THEN
    INSERT INTO company_maturity_level (id, l10n_key, order_index)
    VALUES ('c2109cda-d100-4467-861c-d10b850df51e', 'ETI', 2);
  END IF;
  IF NOT EXISTS(SELECT 1
                FROM company_maturity_level
                WHERE id = 'a6da580e-4e3c-42c0-a3e9-47e1750c2665')
  THEN
    INSERT INTO company_maturity_level (id, l10n_key, order_index)
    VALUES ('a6da580e-4e3c-42c0-a3e9-47e1750c2665', 'Grand groupe', 3);
  END IF;

  -- Reorder existing company maturity levels

  UPDATE company_maturity_level
  SET order_index = 4
  WHERE id = 'ae85da35-e2ab-4bf1-a179-2075e55ec69f';
  UPDATE company_maturity_level
  SET order_index = 5
  WHERE id = 'daf751e6-54f4-471c-859e-c40fba4056d7';
  UPDATE company_maturity_level
  SET order_index = 6
  WHERE id = '55bf2584-fdf0-4f31-8535-c53fd1a983e4';
  UPDATE company_maturity_level
  SET order_index = 7
  WHERE id = 'f43c37ec-311e-4cf9-93a7-6a13d9eb59e9';

  -- Add new commodity types

  IF NOT EXISTS(SELECT 1
                FROM commodity_type
                WHERE id = 'e3572033-4aa5-4c60-904e-6ca013f1ea3e')
  THEN
    INSERT INTO commodity_type (id, l10n_key, order_index)
    VALUES ('e3572033-4aa5-4c60-904e-6ca013f1ea3e', 'Forfait / Projet', 1);
  END IF;

  -- Update existing commodity types

  UPDATE commodity_type
  SET l10n_key = 'Assistance technique / Régie', order_index = 0
  WHERE id = 'b6ada1ca-71fd-4f82-b859-97f9b458bbb5';
  UPDATE commodity_type
  SET l10n_key = 'Logiciels / Licence / SaaS', order_index = 2
  WHERE id = '17102f56-bb40-4766-bcef-89b34b270bb8';

  -- Update task types

  UPDATE task_type
  SET l10n_key = 'Prospection', order_index = 0
  WHERE id = '11535af7-b950-42e8-b13c-95684568e52e';
  UPDATE task_type
  SET l10n_key = 'Ouverture de nouveaux clients', order_index = 1
  WHERE id = 'a1939d6e-0319-44b7-b0ab-28084df879f9';
  UPDATE task_type
  SET l10n_key = 'Gestion de comptes clients', order_index = 2
  WHERE id = '0a423494-e99a-4e00-8d06-7807a40dd056';
  UPDATE task_type
  SET l10n_key = 'Management de commerciaux', order_index = 3
  WHERE id = '24f737e9-da1c-4251-8f47-7cd9f637fd8e';

  -- Create industry types

  IF NOT EXISTS(SELECT 1
                FROM information_schema.tables
                WHERE table_name = 'industry_type')
  THEN
    CREATE TABLE industry_type (
      id          UUID         NOT NULL,
      l10n_key    VARCHAR(255) NOT NULL,
      order_index INT4         NOT NULL,
      PRIMARY KEY (id)
    );
    INSERT INTO industry_type (id, l10n_key, order_index)
    VALUES ('b71886a5-a338-46a1-a358-cea82fbade55', 'IT / Tech', 0);
    INSERT INTO industry_type (id, l10n_key, order_index)
    VALUES ('cfa4fdf6-cbe8-489a-98cf-3985158ff78d', 'Ingénierie / Industrie', 1);
    INSERT INTO industry_type (id, l10n_key, order_index)
    VALUES ('a9ef9833-87b4-4f0e-a19e-1c165b955e99', 'Digital / Marketing', 2);
    INSERT INTO industry_type (id, l10n_key, order_index)
    VALUES ('e54f2f39-a312-4c4d-8c05-c7138bcbdf9e', 'Stratégie / RH', 3);
    CREATE TABLE conditions_industry_types (
      conditions_talent_id UUID NOT NULL,
      industry_types_id    UUID NOT NULL,
      PRIMARY KEY (conditions_talent_id, industry_types_id)
    );
    ALTER TABLE conditions_industry_types
      ADD CONSTRAINT FK_conditions_industry_types__industry_type FOREIGN KEY (industry_types_id) REFERENCES industry_type;
    ALTER TABLE conditions_industry_types
      ADD CONSTRAINT FK_conditions_industry_types__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
  END IF;

  -- Update talent

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE table_name = 'talent' AND column_name = 'number_of_managed_consultants')
  THEN
    ALTER TABLE talent
      ADD number_of_managed_consultants INT4 NOT NULL DEFAULT 0,
      ADD number_of_managed_projects INT4 NOT NULL DEFAULT 0;
  END IF;

END$$;