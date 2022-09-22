CREATE INDEX idx_events_date
    ON events(event_date);

CREATE INDEX idx_events_initiator
    ON events(initiator);

CREATE INDEX idx_events_paid
    ON events(paid);

CREATE INDEX idx_events_state
    ON events(state);