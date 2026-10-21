CREATE TABLE seat_inventory (
     event_id   BIGINT      NOT NULL,
     seat_no    VARCHAR(20) NOT NULL,
     status     VARCHAR(10) NOT NULL DEFAULT 'AVAILABLE',
     booking_id UUID,
     sold_at    TIMESTAMPTZ,
     PRIMARY KEY (event_id, seat_no)
);