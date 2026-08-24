CREATE TABLE providers (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE bookable_slots (
    id UUID PRIMARY KEY,
    provider_id UUID NOT NULL REFERENCES providers(id),
    starts_at TIMESTAMPTZ NOT NULL,
    ends_at TIMESTAMPTZ NOT NULL,
    status VARCHAR(30) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT bookable_slots_time_range CHECK (ends_at > starts_at)
);
CREATE INDEX idx_bookable_slots_provider_time ON bookable_slots(provider_id, starts_at);
CREATE INDEX idx_bookable_slots_available_time ON bookable_slots(status, starts_at);

CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    slot_id UUID NOT NULL REFERENCES bookable_slots(id),
    guest_email VARCHAR(320) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    confirmed_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_reservations_pending_expiry ON reservations(status, expires_at);
CREATE INDEX idx_reservations_slot ON reservations(slot_id);

CREATE TABLE confirmation_tokens (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL UNIQUE REFERENCES reservations(id),
    token_hash CHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ
);