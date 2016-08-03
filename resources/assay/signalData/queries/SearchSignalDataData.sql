-- noinspection SqlDialectInspectionForFile
-- noinspection SqlNoDataSourceInspectionForFile
-- This query is used to define the search criteria as used for the rawHPLC initial view
SELECT
Data.RowId,
--TODO: Identify search fields
COALESCE(Data.Name, '') || COALESCE(Data.Run.RunIdentifier, '')AS Search
FROM Data