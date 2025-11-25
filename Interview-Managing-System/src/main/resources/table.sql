CREATE TABLE IF NOT EXISTS interviews (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    job_position_id BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    scheduled_time TIMESTAMP,
    duration_minutes INTEGER DEFAULT 60,
    meeting_url VARCHAR(500),
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS interview_participants (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT REFERENCES interviews(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    user_type VARCHAR(50) NOT NULL, -- RECRUITER, CANDIDATE, INTERVIEWER
    participant_role VARCHAR(50) NOT NULL, -- OWNER, PARTICIPANT, OBSERVER
    status VARCHAR(50) DEFAULT 'PENDING',
    joined_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS interview_feedback (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT REFERENCES interviews(id) ON DELETE CASCADE,
    participant_id BIGINT REFERENCES interview_participants(id),
    feedback_text TEXT,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    strengths TEXT,
    areas_for_improvement TEXT,
    recommendation VARCHAR(50),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS interview_events (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT REFERENCES interviews(id) ON DELETE CASCADE,
    event_type VARCHAR(100) NOT NULL,
    event_data JSONB,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE IF NOT EXISTS INDEX idx_interviews_status ON interviews(status);
CREATE IF NOT EXISTS INDEX idx_interviews_scheduled_time ON interviews(scheduled_time);
CREATE IF NOT EXISTS INDEX idx_participants_interview_id ON interview_participants(interview_id);
CREATE IF NOT EXISTS INDEX idx_participants_user_id ON interview_participants(user_id);


CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(500) NOT NULL,
    interview_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    recipient_user_id VARCHAR(255)
);
CREATE IF NOT EXISTS INDEX idx_notifications_created_at ON notifications(created_at DESC);
CREATE IF NOT EXISTS INDEX idx_notifications_read_status ON notifications(is_read);
CREATE IF NOT EXISTS INDEX idx_notifications_recipient ON notifications(recipient_user_id);