-- EXTENSIONS ======================================================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USER ============================================================================================
INSERT INTO users (id, username, password, first_name, last_name, phone_number, authorities, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_signed_in_at) VALUES ('eb4b1008-4760-4bde-a3c0-e62b65796add', 'admin@deepfish.fr', '$2a$10$zsz1JpJoBGfa0arhyikjN.8w4umkcxWlvnMT/LUaNWE69W0fa1Lom', 'Martin', 'Jeannot', '0000000000', 'ROLE_SUPER_ADMIN,ROLE_ADMIN', TRUE, TRUE, TRUE, TRUE, '2018-01-01 00:00:00.000', '2018-01-01 00:00:00.000');
INSERT INTO users (id, username, password, first_name, last_name, phone_number, authorities, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_signed_in_at) VALUES ('af4f0340-543e-4d99-bed7-597844e250da', 'david@deepfish.fr', '$2a$10$62/Q4wB6DqOn40816.2g2OmSMkhctcCb2duyHmdj25bWkoTanUXDi', 'David', 'Branellec', '0000000000', 'ROLE_SUPER_ADMIN,ROLE_ADMIN', TRUE, TRUE, TRUE, TRUE, '2018-01-01 00:00:00.000', '2018-01-01 00:00:00.000');

-- COMPANY MATURITY LEVEL ==========================================================================
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('6ae9ce1a-cbef-41b7-bf3a-6ea6f85bcda9', 'Jeune Startup');
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('44df03f2-d552-4143-a7cb-5fc926a57542', 'Startup Mature/PME');
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('a79199a8-3783-416f-a0fa-e9727627f8b2', 'Grand Groupe');

-- JOB TYPE ========================================================================================
INSERT INTO job_type (id, l10n_key) VALUES ('3ddd469d-3683-4b7a-8aa2-060ce3adfc80', 'Pre-Sales');
INSERT INTO job_type (id, l10n_key) VALUES ('a4830521-c50e-4b91-ac90-da922d28c49e', 'Sales');
INSERT INTO job_type (id, l10n_key) VALUES ('e1c36e80-6cf7-4715-9239-a152e6cda361', 'Customer Success');

-- COMMODITY TYPE ==================================================================================
INSERT INTO commodity_type (id, l10n_key) VALUES ('17102f56-bb40-4766-bcef-89b34b270bb8', 'Saas / Licence');
INSERT INTO commodity_type (id, l10n_key) VALUES ('b6ada1ca-71fd-4f82-b859-97f9b458bbb5', 'Service');

-- TASK TYPE =======================================================================================
INSERT INTO task_type (id, l10n_key) VALUES ('11535af7-b950-42e8-b13c-95684568e52e', 'Cold calling');
INSERT INTO task_type (id, l10n_key) VALUES ('a1939d6e-0319-44b7-b0ab-28084df879f9', 'New business');
INSERT INTO task_type (id, l10n_key) VALUES ('0a423494-e99a-4e00-8d06-7807a40dd056', 'Account management');
INSERT INTO task_type (id, l10n_key) VALUES ('24f737e9-da1c-4251-8f47-7cd9f637fd8e', 'Gestion d''équipe');

-- FIXED LOCATION ==================================================================================
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('1a00b389-46e6-4cfc-8c16-051e8f9bb296', 'France', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('45051b1f-6dbd-486b-b078-97baad2ac4b0', 'Paris', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('aec5ecb9-2897-4de7-8e7e-0272234da5d1', 'Lyon', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('7770086a-61ef-4abb-b063-79dc56f4dbef', 'Marseille', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('1a0121c7-4968-4b4b-9a1a-4194a6ebfdb9', 'Lille', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('2df62796-4e57-4b55-b109-d4276c45a2bc', 'Toulouse', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('6f6fe222-6487-47c1-95db-8b3928f887b0', 'Nantes', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('c69887fd-72d7-4133-b768-22ddf74c8777', 'Bordeaux', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('4f94d585-4fd0-471b-9cd5-dde7302728fe', 'Montpellier', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('a60ee3bc-592e-4dd8-b7b8-9f5e5cf4a3af', 'Nice', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('c49dd4d0-7e9b-4cfb-963a-229636675f55', 'Rennes', '1a00b389-46e6-4cfc-8c16-051e8f9bb296');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('7ae48866-52ba-4be3-8ed3-6b4f4b2041b0', 'Espagne', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('f75686c3-9c1a-4180-b89a-1ee31b30f919', 'Madrid', '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('0d71ccad-9eff-4068-a593-e3bcac17d711', 'Barcelone', '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('76f41df0-d14d-477e-8df9-47a86a080876', 'Malaga', '7ae48866-52ba-4be3-8ed3-6b4f4b2041b0');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('788752c0-6811-41d9-81f6-890c9024ecb7', 'Irlande', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('9db6a8e1-855a-4570-b6a0-06e3a17bcbcc', 'Dublin', '788752c0-6811-41d9-81f6-890c9024ecb7');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('e799734a-db17-479c-a1ed-302ac593778a', 'Cork', '788752c0-6811-41d9-81f6-890c9024ecb7');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('fbcbf848-5686-47f0-98bc-1cd92f3dd837', 'Royaume-Uni', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('c59f21d0-058e-400d-b079-719a5fb47a39', 'London', 'fbcbf848-5686-47f0-98bc-1cd92f3dd837');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('7aa418bb-eac8-4d3e-b0b8-ab2879e4c350', 'Birmingham', 'fbcbf848-5686-47f0-98bc-1cd92f3dd837');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c', 'Allemagne', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('e78b2678-0d69-4bf9-b613-8ed7bbdc250e', 'Berlin', '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('951bdc73-aab7-401d-8af1-dc975d933625', 'Cologne', '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('720c6372-d91f-494c-9874-7652bb272bf7', 'Munich', '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('20ae20b3-4833-4bc1-be13-796e0bf5bfd2', 'Frankfurt', '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('159c2574-9d57-4ec3-a0f8-c5be085df8bb', 'Hambourg', '2d100fb6-3fc1-4c32-a8a7-5cddf3337b1c');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('ed49da57-4fde-4bbc-b280-9d755e31f605', 'Benelux', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('6e6727ae-068a-4a33-82e1-72b49beea37f', 'Amsterdam', 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('df35d69d-406d-4446-b050-0b22b8b29b30', 'Bruxelles', 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('6a028cb7-57d7-4018-bd1e-c8ada63ad2c6', 'Luxembourg', 'ed49da57-4fde-4bbc-b280-9d755e31f605');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('68d5b9d2-0984-4627-afc7-90eb22563cd9', 'Italie', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('d0dfcd42-676a-4929-a6f9-81394c3d6cde', 'Rome', '68d5b9d2-0984-4627-afc7-90eb22563cd9');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('186b1c56-8c25-4923-8294-8e404cbb74d4', 'Milan', '68d5b9d2-0984-4627-afc7-90eb22563cd9');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('f9ec8dd9-d9c3-41f9-8027-13ef41e949f7', 'Portugal', NULL);
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('69d30103-8c75-445f-b997-eca94f6f23a8', 'Lisbonne', 'f9ec8dd9-d9c3-41f9-8027-13ef41e949f7');
INSERT INTO fixed_location (id, l10n_key, parent_location_id) VALUES ('dc7b5bf5-e935-487c-b52b-43554c0a3f43', 'Porto', 'f9ec8dd9-d9c3-41f9-8027-13ef41e949f7');

-- SENIORITY =======================================================================================
INSERT INTO seniority (id, l10n_key, order_index) VALUES ('6780434e-2512-4f2a-96aa-01c5ee10b7ec', 'Junior', 0);
INSERT INTO seniority (id, l10n_key, order_index) VALUES ('3fd3a132-f78a-4897-ba2b-440517a6db8f', 'Confirmé', 1);
INSERT INTO seniority (id, l10n_key, order_index) VALUES ('fc373f87-9cbf-4460-a59a-2843d997ea15', 'Senior', 2);
