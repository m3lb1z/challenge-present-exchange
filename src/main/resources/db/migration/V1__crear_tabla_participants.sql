CREATE TABLE participants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    gift_recipient_id INTEGER,
    FOREIGN KEY (gift_recipient_id) REFERENCES participants(id),
    CONSTRAINT no_self_assignment CHECK (gift_recipient_id != id)
);

CREATE INDEX idx_participants_email ON participants(email);
CREATE INDEX idx_participants_gift_recipient ON participants(gift_recipient_id);