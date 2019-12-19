DO $$
BEGIN

  IF NOT EXISTS(SELECT 1
                FROM job_type
                WHERE id = 'b92fa136-7512-4fa5-bf23-95049121affc')
  THEN
    INSERT INTO job_type (id, l10n_key, order_index, enabled)
    VALUES
      ('b92fa136-7512-4fa5-bf23-95049121affc', E'Ingénieur d\'affaires / Business Manager', 3,
       TRUE),
      ('be19086b-833f-4730-b146-4e482b4f4501', E'Responsable d\'agence / Directeur d\'agence', 4,
       TRUE),
      ('aa84c4c1-4c9a-4763-b9cb-845cb86820fb', 'Directeur commercial', 5, TRUE),
      ('dc833c7c-24df-43fe-8f0b-e438e65eaa0c',
       'Chargé(e) de recrutement / Talent acquisition specialist', 6, TRUE),
      ('e0e4678d-6ba0-49e5-90f6-58adaadf778e', 'Responsable recrutement / Manager RH', 7, TRUE);
  END IF;

END$$;