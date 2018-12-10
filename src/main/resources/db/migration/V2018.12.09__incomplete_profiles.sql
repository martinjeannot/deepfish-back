DO $$
BEGIN

  -- Incomplete profiles related modifications =====================================================

  -- Update talent

  IF NOT EXISTS(SELECT 1
                FROM information_schema.columns
                WHERE
                  table_name = 'talent' AND column_name = 'profile_completeness_last_calculated_at')
  THEN
    ALTER TABLE talent
      ADD profile_completeness_last_calculated_at TIMESTAMP NOT NULL DEFAULT '2018-01-01 00:00:00.000',
      ADD profile_completeness_last_updated_at TIMESTAMP NOT NULL DEFAULT '2018-01-01 00:00:00.000';
  END IF;

  -- Expired opportunities =========================================================================

  -- Helping the first opportunity expiration step run

  UPDATE opportunity
  SET talent_status = 'EXPIRED'
  WHERE talent_status = 'PENDING' AND created_at < '2018-11-23 00:00:00';

  -- New fixed location ============================================================================

  IF NOT EXISTS(SELECT 1
                FROM fixed_location
                WHERE id = 'eeda3d46-3e93-42a7-8eed-82c511f98594')
  THEN
    INSERT INTO fixed_location (id, l10n_key, order_index, parent_location_id, enabled)
    VALUES ('eeda3d46-3e93-42a7-8eed-82c511f98594', 'Strasbourg', 0,
            '1a00b389-46e6-4cfc-8c16-051e8f9bb296', TRUE);
  END IF;

  -- Update remaining fr mail addresses ============================================================

  UPDATE users
  SET username = 'david@deepfish.co'
  WHERE username = 'david@deepfish.fr';

  UPDATE users
  SET username = 'martin@deepfish.co'
  WHERE username = 'admin@deepfish.fr';

END$$;