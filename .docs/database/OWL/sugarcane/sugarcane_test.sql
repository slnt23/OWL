SELECT
    pi.id AS item_id,
    pi.item_name,
    pi.unit,
    pi.specification,
    pc.category_name,
    gl.location_name,
    pr.price,
    pr.currency,
    pr.price_unit,
    ps.source_name,
    ps.reliability_level,
    pr.effective_time,
    pr.confidence
FROM price_record pr
         INNER JOIN price_item pi ON pr.item_id = pi.id
         INNER JOIN price_category pc ON pi.category_id = pc.id
         INNER JOIN geo_location gl ON pr.location_id = gl.id
         INNER JOIN price_source ps ON pr.source_id = ps.id
         INNER JOIN (
    -- 找到每个物品+地点的最新价格记录ID
    SELECT
        item_id,
        location_id,
        MAX(effective_time) AS max_effective_time
    FROM price_record
    WHERE item_id = 1   -- 这里传入你要查询的物品ID
    GROUP BY item_id, location_id
) latest ON pr.item_id = latest.item_id
    AND pr.location_id = latest.location_id
    AND pr.effective_time = latest.max_effective_time
WHERE pi.id = 1  -- 同样限制物品ID
ORDER BY gl.id;


SELECT
    pi.id AS item_id,
    pi.item_name,
    pi.unit,
    pi.specification,
    pc.category_name,
    gl.location_name,
    pr.price,
    pr.currency,
    pr.price_unit,
    ps.source_name,
    ps.reliability_level,
    pr.effective_time,
    pr.confidence
FROM price_record pr
         INNER JOIN price_item pi ON pr.item_id = pi.id
         INNER JOIN price_category pc ON pi.category_id = pc.id
         INNER JOIN geo_location gl ON pr.location_id = gl.id
         INNER JOIN price_source ps ON pr.source_id = ps.id
WHERE pr.item_id = 3
  AND pr.location_id = 1
  AND pr.currency = 'CNY'
ORDER BY pr.effective_time DESC
LIMIT 1;