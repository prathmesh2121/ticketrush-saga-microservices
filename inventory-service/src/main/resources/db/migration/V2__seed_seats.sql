   INSERT INTO seat_inventory (event_id, seat_no)
   SELECT 1, 'A' || g FROM generate_series(1, 100) AS g;