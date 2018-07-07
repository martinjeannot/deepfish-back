-- BASE SCHEMA =====================================================================================

CREATE TABLE commodity_type (
  id          UUID         NOT NULL,
  l10n_key    VARCHAR(255) NOT NULL,
  order_index INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE company (
  id          UUID         NOT NULL,
  created_at  TIMESTAMP    NOT NULL,
  description TEXT         NOT NULL,
  name        VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE company_maturity_level (
  id          UUID         NOT NULL,
  l10n_key    VARCHAR(255) NOT NULL,
  order_index INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE conditions (
  can_start_on DATE           NOT NULL,
  fixed_salary NUMERIC(19, 2) NOT NULL CHECK (fixed_salary >= 0),
  talent_id    UUID           NOT NULL,
  PRIMARY KEY (talent_id)
);
CREATE TABLE conditions_commodity_types (
  conditions_talent_id UUID NOT NULL,
  commodity_types_id   UUID NOT NULL,
  PRIMARY KEY (conditions_talent_id, commodity_types_id)
);
CREATE TABLE conditions_company_maturity_levels (
  conditions_talent_id       UUID NOT NULL,
  company_maturity_levels_id UUID NOT NULL,
  PRIMARY KEY (conditions_talent_id, company_maturity_levels_id)
);
CREATE TABLE conditions_fixed_locations (
  conditions_talent_id UUID NOT NULL,
  fixed_locations_id   UUID NOT NULL,
  PRIMARY KEY (conditions_talent_id, fixed_locations_id)
);
CREATE TABLE conditions_job_types (
  conditions_talent_id UUID NOT NULL,
  job_types_id         UUID NOT NULL,
  PRIMARY KEY (conditions_talent_id, job_types_id)
);
CREATE TABLE conditions_task_types (
  conditions_talent_id UUID NOT NULL,
  task_types_id        UUID NOT NULL,
  PRIMARY KEY (conditions_talent_id, task_types_id)
);
CREATE TABLE employer (
  id                      UUID         NOT NULL,
  account_non_expired     BOOLEAN      NOT NULL,
  account_non_locked      BOOLEAN      NOT NULL,
  authorities             VARCHAR(255) NOT NULL,
  created_at              TIMESTAMP    NOT NULL,
  credentials_non_expired BOOLEAN      NOT NULL,
  enabled                 BOOLEAN      NOT NULL,
  first_name              VARCHAR(255) NOT NULL,
  last_name               VARCHAR(255) NOT NULL,
  last_signed_in_at       TIMESTAMP    NOT NULL,
  password                VARCHAR(255) NOT NULL,
  phone_number            VARCHAR(255) NOT NULL,
  username                VARCHAR(255) NOT NULL,
  company_id              UUID         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE fixed_location (
  id                 UUID         NOT NULL,
  l10n_key           VARCHAR(255) NOT NULL,
  order_index        INT4         NOT NULL,
  parent_location_id UUID,
  PRIMARY KEY (id)
);
CREATE TABLE job_type (
  id          UUID         NOT NULL,
  l10n_key    VARCHAR(255) NOT NULL,
  order_index INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE opportunity (
  id                          UUID         NOT NULL,
  created_at                  TIMESTAMP    NOT NULL,
  employer_declination_reason TEXT         NOT NULL,
  employer_status             VARCHAR(255),
  forwarded                   BOOLEAN      NOT NULL,
  forwarded_at                TIMESTAMP,
  pitch                       TEXT         NOT NULL,
  talent_declination_reason   TEXT         NOT NULL,
  talent_status               VARCHAR(255) NOT NULL,
  creator_id                  UUID         NOT NULL,
  requirement_id              UUID         NOT NULL,
  talent_id                   UUID         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE qualification (
  complex_selling_skills_rating INT4 NOT NULL,
  hunting_skills_rating         INT4 NOT NULL,
  ranking                       INT4 NOT NULL,
  recommendation                TEXT NOT NULL,
  technical_skills_rating       INT4 NOT NULL,
  talent_id                     UUID NOT NULL,
  PRIMARY KEY (talent_id)
);
CREATE TABLE requirement (
  id                UUID           NOT NULL,
  created_at        TIMESTAMP      NOT NULL,
  created_by        UUID           NOT NULL,
  fixed_salary      NUMERIC(19, 2) NOT NULL CHECK (fixed_salary >= 0),
  location          VARCHAR(255)   NOT NULL,
  name              VARCHAR(255)   NOT NULL,
  notes             TEXT           NOT NULL,
  opportunity_pitch TEXT           NOT NULL,
  company_id        UUID           NOT NULL,
  job_type_id       UUID           NOT NULL,
  seniority_id      UUID           NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE seniority (
  id          UUID         NOT NULL,
  l10n_key    VARCHAR(255) NOT NULL,
  order_index INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE talent (
  id                      UUID         NOT NULL,
  account_non_expired     BOOLEAN      NOT NULL,
  account_non_locked      BOOLEAN      NOT NULL,
  authorities             VARCHAR(255) NOT NULL,
  created_at              TIMESTAMP    NOT NULL,
  credentials_non_expired BOOLEAN      NOT NULL,
  enabled                 BOOLEAN      NOT NULL,
  first_name              VARCHAR(255) NOT NULL,
  last_name               VARCHAR(255) NOT NULL,
  last_signed_in_at       TIMESTAMP    NOT NULL,
  password                VARCHAR(255) NOT NULL,
  phone_number            VARCHAR(255) NOT NULL,
  username                VARCHAR(255) NOT NULL,
  active                  BOOLEAN      NOT NULL,
  basic_profile           JSONB        NOT NULL,
  basic_profile_text      TEXT         NOT NULL,
  email                   VARCHAR(255) NOT NULL,
  full_profile_text       TEXT         NOT NULL,
  linked_in_id            VARCHAR(255) NOT NULL,
  maturity_level          VARCHAR(255),
  notes                   TEXT         NOT NULL,
  profile_completeness    FLOAT4       NOT NULL,
  self_pitch              TEXT         NOT NULL,
  years_of_experience     INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE task_type (
  id          UUID         NOT NULL,
  l10n_key    VARCHAR(255) NOT NULL,
  order_index INT4         NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE users (
  id                      UUID         NOT NULL,
  account_non_expired     BOOLEAN      NOT NULL,
  account_non_locked      BOOLEAN      NOT NULL,
  authorities             VARCHAR(255) NOT NULL,
  created_at              TIMESTAMP    NOT NULL,
  credentials_non_expired BOOLEAN      NOT NULL,
  enabled                 BOOLEAN      NOT NULL,
  first_name              VARCHAR(255) NOT NULL,
  last_name               VARCHAR(255) NOT NULL,
  last_signed_in_at       TIMESTAMP    NOT NULL,
  password                VARCHAR(255) NOT NULL,
  phone_number            VARCHAR(255) NOT NULL,
  username                VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE employer
  ADD CONSTRAINT UK_employer__username UNIQUE (username);
ALTER TABLE talent
  ADD CONSTRAINT UK_talent__username UNIQUE (username);
ALTER TABLE talent
  ADD CONSTRAINT UK_talent__linked_in_id UNIQUE (linked_in_id);
ALTER TABLE users
  ADD CONSTRAINT UK_users__username UNIQUE (username);
ALTER TABLE conditions
  ADD CONSTRAINT FK_conditions__talent__talent_id FOREIGN KEY (talent_id) REFERENCES talent;
ALTER TABLE conditions_commodity_types
  ADD CONSTRAINT FK_conditions_commodity_types__commodity_type FOREIGN KEY (commodity_types_id) REFERENCES commodity_type;
ALTER TABLE conditions_commodity_types
  ADD CONSTRAINT FK_conditions_commodity_types__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
ALTER TABLE conditions_company_maturity_levels
  ADD CONSTRAINT FK_conditions_company_maturity_levels__company_maturity_level FOREIGN KEY (company_maturity_levels_id) REFERENCES company_maturity_level;
ALTER TABLE conditions_company_maturity_levels
  ADD CONSTRAINT FK_conditions_company_maturity_levels__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
ALTER TABLE conditions_fixed_locations
  ADD CONSTRAINT FK_conditions_fixed_locations__fixed_location FOREIGN KEY (fixed_locations_id) REFERENCES fixed_location;
ALTER TABLE conditions_fixed_locations
  ADD CONSTRAINT FK_conditions_fixed_locations__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
ALTER TABLE conditions_job_types
  ADD CONSTRAINT FK_conditions_job_types__job_type FOREIGN KEY (job_types_id) REFERENCES job_type;
ALTER TABLE conditions_job_types
  ADD CONSTRAINT FK_conditions_job_types__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
ALTER TABLE conditions_task_types
  ADD CONSTRAINT FK_conditions_task_types__task_type FOREIGN KEY (task_types_id) REFERENCES task_type;
ALTER TABLE conditions_task_types
  ADD CONSTRAINT FK_conditions_task_types__conditions FOREIGN KEY (conditions_talent_id) REFERENCES conditions;
ALTER TABLE employer
  ADD CONSTRAINT FK_employer__company__company_id FOREIGN KEY (company_id) REFERENCES company;
ALTER TABLE fixed_location
  ADD CONSTRAINT FK_fixed_location__fixed_location__parent_location_id FOREIGN KEY (parent_location_id) REFERENCES fixed_location;
ALTER TABLE opportunity
  ADD CONSTRAINT FK_opportunity__user__creator_id FOREIGN KEY (creator_id) REFERENCES users;
ALTER TABLE opportunity
  ADD CONSTRAINT FK_opportunity__requirement__requirement_id FOREIGN KEY (requirement_id) REFERENCES requirement;
ALTER TABLE opportunity
  ADD CONSTRAINT FK_opportunity__talent__talent_id FOREIGN KEY (talent_id) REFERENCES talent;
ALTER TABLE qualification
  ADD CONSTRAINT FK_qualification__talent__talent_id FOREIGN KEY (talent_id) REFERENCES talent;
ALTER TABLE requirement
  ADD CONSTRAINT FK_requirement__company__company_id FOREIGN KEY (company_id) REFERENCES company;
ALTER TABLE requirement
  ADD CONSTRAINT FK_requirement__job_type__job_type_id FOREIGN KEY (job_type_id) REFERENCES job_type;
ALTER TABLE requirement
  ADD CONSTRAINT FK_requirement__seniority__seniority_id FOREIGN KEY (seniority_id) REFERENCES seniority;

-- EXTENSIONS ======================================================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USER ============================================================================================
INSERT INTO users (id, username, password, first_name, last_name, phone_number, authorities, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_signed_in_at)
VALUES ('eb4b1008-4760-4bde-a3c0-e62b65796add', 'admin@deepfish.fr',
                                                '$2a$10$zsz1JpJoBGfa0arhyikjN.8w4umkcxWlvnMT/LUaNWE69W0fa1Lom',
                                                'Martin', 'Jeannot', '0000000000',
                                                'ROLE_SUPER_ADMIN,ROLE_ADMIN', TRUE, TRUE, TRUE,
                                                TRUE, '2018-01-01 00:00:00.000',
        '2018-01-01 00:00:00.000');
INSERT INTO users (id, username, password, first_name, last_name, phone_number, authorities, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_signed_in_at)
VALUES ('af4f0340-543e-4d99-bed7-597844e250da', 'david@deepfish.fr',
                                                '$2a$10$62/Q4wB6DqOn40816.2g2OmSMkhctcCb2duyHmdj25bWkoTanUXDi',
                                                'David', 'Branellec', '0000000000',
                                                'ROLE_SUPER_ADMIN,ROLE_ADMIN', TRUE, TRUE, TRUE,
                                                TRUE, '2018-01-01 00:00:00.000',
        '2018-01-01 00:00:00.000');

-- COMPANY MATURITY LEVEL ==========================================================================
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('6ae9ce1a-cbef-41b7-bf3a-6ea6f85bcda9', 'Jeune Startup', 0);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('44df03f2-d552-4143-a7cb-5fc926a57542', 'Startup Mature/PME', 0);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('a79199a8-3783-416f-a0fa-e9727627f8b2', 'Grand Groupe', 0);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('ae85da35-e2ab-4bf1-a179-2075e55ec69f', 'Amorçage', 0);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('daf751e6-54f4-471c-859e-c40fba4056d7', 'Early stage', 1);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('55bf2584-fdf0-4f31-8535-c53fd1a983e4', 'Accélération', 2);
INSERT INTO company_maturity_level (id, l10n_key, order_index)
VALUES ('f43c37ec-311e-4cf9-93a7-6a13d9eb59e9', 'Maturité', 3);

-- JOB TYPE ========================================================================================
INSERT INTO job_type (id, l10n_key, order_index)
VALUES ('3ddd469d-3683-4b7a-8aa2-060ce3adfc80', 'Pre-Sales', 1);
INSERT INTO job_type (id, l10n_key, order_index)
VALUES ('a4830521-c50e-4b91-ac90-da922d28c49e', 'Sales', 0);
INSERT INTO job_type (id, l10n_key, order_index)
VALUES ('e1c36e80-6cf7-4715-9239-a152e6cda361', 'Customer Success', 2);

-- COMMODITY TYPE ==================================================================================
INSERT INTO commodity_type (id, l10n_key, order_index)
VALUES ('17102f56-bb40-4766-bcef-89b34b270bb8', 'Saas / Licence', 0);
INSERT INTO commodity_type (id, l10n_key, order_index)
VALUES ('b6ada1ca-71fd-4f82-b859-97f9b458bbb5', 'Service', 1);

-- TASK TYPE =======================================================================================
INSERT INTO task_type (id, l10n_key, order_index)
VALUES ('11535af7-b950-42e8-b13c-95684568e52e', 'Cold calling', 0);
INSERT INTO task_type (id, l10n_key, order_index)
VALUES ('a1939d6e-0319-44b7-b0ab-28084df879f9', 'New business', 0);
INSERT INTO task_type (id, l10n_key, order_index)
VALUES ('0a423494-e99a-4e00-8d06-7807a40dd056', 'Account management', 0);
INSERT INTO task_type (id, l10n_key, order_index)
VALUES ('24f737e9-da1c-4251-8f47-7cd9f637fd8e', 'Gestion d''équipe', 0);

-- FIXED LOCATION ==================================================================================
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('1a00b389-46e6-4cfc-8c16-051e8f9bb296', 'France', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('45051b1f-6dbd-486b-b078-97baad2ac4b0', 'Paris', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('aec5ecb9-2897-4de7-8e7e-0272234da5d1', 'Lyon', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('7770086a-61ef-4abb-b063-79dc56f4dbef', 'Marseille', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('1a0121c7-4968-4b4b-9a1a-4194a6ebfdb9', 'Lille', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('2df62796-4e57-4b55-b109-d4276c45a2bc', 'Toulouse', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('6f6fe222-6487-47c1-95db-8b3928f887b0', 'Nantes', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('c69887fd-72d7-4133-b768-22ddf74c8777', 'Bordeaux', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('4f94d585-4fd0-471b-9cd5-dde7302728fe', 'Montpellier', 0,
   '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('a60ee3bc-592e-4dd8-b7b8-9f5e5cf4a3af', 'Nice', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('c49dd4d0-7e9b-4cfb-963a-229636675f55', 'Rennes', 0, '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('7ae48866-52ba-4be3-8ed3-6b4f4b2041b0', 'Espagne', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('f75686c3-9c1a-4180-b89a-1ee31b30f919', 'Madrid', 0, '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('0d71ccad-9eff-4068-a593-e3bcac17d711', 'Barcelone', 0, '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('76f41df0-d14d-477e-8df9-47a86a080876', 'Malaga', 0, '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('788752c0-6811-41d9-81f6-890c9024ecb7', 'Irlande', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('9db6a8e1-855a-4570-b6a0-06e3a17bcbcc', 'Dublin', 0, '788752c0-6811-41d9-81f6-890c9024ecb7');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('e799734a-db17-479c-a1ed-302ac593778a', 'Cork', 0, '788752c0-6811-41d9-81f6-890c9024ecb7');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('fbcbf848-5686-47f0-98bc-1cd92f3dd837', 'Royaume-Uni', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('c59f21d0-058e-400d-b079-719a5fb47a39', 'London', 0, 'fbcbf848-5686-47f0-98bc-1cd92f3dd837');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('7aa418bb-eac8-4d3e-b0b8-ab2879e4c350', 'Birmingham', 0, 'fbcbf848-5686-47f0-98bc-1cd92f3dd837');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c', 'Allemagne', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('e78b2678-0d69-4bf9-b613-8ed7bbdc250e', 'Berlin', 0, '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('951bdc73-aab7-401d-8af1-dc975d933625', 'Cologne', 0, '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('720c6372-d91f-494c-9874-7652bb272bf7', 'Munich', 0, '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('20ae20b3-4833-4bc1-be13-796e0bf5bfd2', 'Frankfurt', 0, '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('159c2574-9d57-4ec3-a0f8-c5be085df8bb', 'Hambourg', 0, '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('ed49da57-4fde-4bbc-b280-9d755e31f605', 'Benelux', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('6e6727ae-068a-4a33-82e1-72b49beea37f', 'Amsterdam', 0, 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('df35d69d-406d-4446-b050-0b22b8b29b30', 'Bruxelles', 0, 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id) VALUES
  ('6a028cb7-57d7-4018-bd1e-c8ada63ad2c6', 'Luxembourg', 0, 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('68d5b9d2-0984-4627-afc7-90eb22563cd9', 'Italie', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('d0dfcd42-676a-4929-a6f9-81394c3d6cde', 'Rome', 0, '68d5b9d2-0984-4627-afc7-90eb22563cd9');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('186b1c56-8c25-4923-8294-8e404cbb74d4', 'Milan', 0, '68d5b9d2-0984-4627-afc7-90eb22563cd9');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('f9ec8dd9-d9c3-41f9-8027-13ef41e949f7', 'Portugal', 0, NULL);
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES
  ('69d30103-8c75-445f-b997-eca94f6f23a8', 'Lisbonne', 0, 'f9ec8dd9-d9c3-41f9-8027-13ef41e949f7');
INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id)
VALUES ('dc7b5bf5-e935-487c-b52b-43554c0a3f43', 'Porto', 0, 'f9ec8dd9-d9c3-41f9-8027-13ef41e949f7');

-- SENIORITY =======================================================================================
INSERT INTO seniority (id, l10n_key, order_index)
VALUES ('6780434e-2512-4f2a-96aa-01c5ee10b7ec', 'Junior', 0);
INSERT INTO seniority (id, l10n_key, order_index)
VALUES ('3fd3a132-f78a-4897-ba2b-440517a6db8f', 'Confirmé', 1);
INSERT INTO seniority (id, l10n_key, order_index)
VALUES ('fc373f87-9cbf-4460-a59a-2843d997ea15', 'Senior', 2);
