CREATE TABLE IF NOT EXISTS interview_slots (
    id BIGSERIAL PRIMARY KEY,

    interviewer_id BIGINT NOT NULL,

    start_time TIMESTAMP NOT NULL,

    end_time TIMESTAMP NOT NULL,

    status VARCHAR(50) NOT NULL,

    scheduled_by BIGINT,

    cancelled_by BIGINT,

    cancellation_reason TEXT,

    created_at TIMESTAMP DEFAULT NOW(),

    updated_at TIMESTAMP DEFAULT NOW()
);
