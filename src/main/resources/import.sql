-- EXTENSIONS ======================================================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- COMPANY MATURITY LEVEL ==========================================================================
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('6ae9ce1a-cbef-41b7-bf3a-6ea6f85bcda9', 'Jeune Startup');
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('44df03f2-d552-4143-a7cb-5fc926a57542', 'Startup Mature/PME');
INSERT INTO company_maturity_level (id, l10n_key) VALUES ('a79199a8-3783-416f-a0fa-e9727627f8b2', 'Grand Groupe');

-- JOBS ============================================================================================
INSERT INTO job (id, title) VALUES ('3ddd469d-3683-4b7a-8aa2-060ce3adfc80', 'Pre-Sales');
INSERT INTO job (id, title) VALUES ('a4830521-c50e-4b91-ac90-da922d28c49e', 'Sales');
INSERT INTO job (id, title) VALUES ('e1c36e80-6cf7-4715-9239-a152e6cda361', 'Customer Success');

-- COMMODITY TYPE ==================================================================================
INSERT INTO commodity_type (id, l10n_key) VALUES ('17102f56-bb40-4766-bcef-89b34b270bb8', 'Saas / Licence');
INSERT INTO commodity_type (id, l10n_key) VALUES ('b6ada1ca-71fd-4f82-b859-97f9b458bbb5', 'Service');

-- TASK TYPE =======================================================================================
INSERT INTO task_type (id, l10n_key) VALUES ('11535af7-b950-42e8-b13c-95684568e52e', 'Cold calling');
INSERT INTO task_type (id, l10n_key) VALUES ('a1939d6e-0319-44b7-b0ab-28084df879f9', 'New business');
INSERT INTO task_type (id, l10n_key) VALUES ('0a423494-e99a-4e00-8d06-7807a40dd056', 'Account management');
INSERT INTO task_type (id, l10n_key) VALUES ('24f737e9-da1c-4251-8f47-7cd9f637fd8e', 'Gestion d''équipe');
