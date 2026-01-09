CREATE TABLE notification_history (
    id BIGSERIAL PRIMARY KEY,
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255),
    message TEXT,
    type VARCHAR(50),
    status VARCHAR(50),
    provider_response TEXT,
    reference_id VARCHAR(100) UNIQUE,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
