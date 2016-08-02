-- This query is used to define the search criteria as used for the rawHPLC initial view
SELECT
Data.RowId,
COALESCE(Data.Name, '') || COALESCE(Data.Run.RunIdentifier, '') || COALESCE(Data.Run.Machine, '') AS Search
FROM Data