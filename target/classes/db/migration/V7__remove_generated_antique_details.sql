UPDATE antiques a
SET detail = ''
WHERE BTRIM(a.detail) = a.subcategory
  AND NOT EXISTS (
      SELECT 1
      FROM categories c
      WHERE c.antique_type = a.type
        AND c.subcategory_key = a.subcategory
        AND c.detail_key IS NOT NULL
  );
