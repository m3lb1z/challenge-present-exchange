ALTER TABLE participants 
ADD COLUMN user_id BIGINT UNIQUE;

ALTER TABLE participants
ADD CONSTRAINT fk_participants_users
FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_participants_user ON participants(user_id);
