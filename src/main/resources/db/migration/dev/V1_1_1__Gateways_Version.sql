ALTER TABLE gateways ADD COLUMN version VARCHAR(255) NOT NULL DEFAULT '0.1';
UPDATE gateways SET version='0.1';
ALTER TABLE gateways ALTER COLUMN created_at SET NOT NULL;
ALTER TABLE gateways ALTER COLUMN "identity" SET NOT NULL;
ALTER TABLE gateways ALTER COLUMN status SET NOT NULL;