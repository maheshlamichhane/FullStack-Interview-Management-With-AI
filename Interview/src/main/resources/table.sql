CREATE TABLE IF NOT EXISTS interviews (
    id BIGSERIAL PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    interviewer_id BIGINT NOT NULL,
    job_position_id BIGINT,
    interview_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    title VARCHAR(255),a
    description TEXT,
    scheduled_start_time TIMESTAMP NOT NULL,
    scheduled_end_time TIMESTAMP NOT NULL,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    duration_minutes INTEGER,
    meeting_link VARCHAR(500),
    location VARCHAR(255),
    notes TEXT,
    overall_rating DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
GRANT ALL PRIVILEGES ON TABLE interviews TO ad_java;
ALTER TABLE interviews OWNER TO ad_java;

CREATE IF NOT EXISTS INDEX idx_interviews_candidate_id ON interviews(candidate_id);
CREATE IF NOT EXISTS INDEX idx_interviews_interviewer_id ON interviews(interviewer_id);
CREATE IF NOT EXISTS INDEX idx_interviews_job_position_id ON interviews(job_position_id);
CREATE IF NOT EXISTS INDEX idx_interviews_scheduled_start_time ON interviews(scheduled_start_time);
CREATE IF NOT EXISTS INDEX idx_interviews_status ON interviews(status);
CREATE IF NOT EXISTS INDEX idx_interviews_interview_type ON interviews(interview_type);
CREATE IF NOT EXISTS INDEX idx_interviews_created_at ON interviews(created_at);
-------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS interview_participants (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    participant_id BIGINT NOT NULL,
    participant_type VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    confirmed_attendance BOOLEAN DEFAULT FALSE,
    attended BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
GRANT ALL PRIVILEGES ON TABLE interview_participants TO ad_java;
ALTER TABLE interview_participants OWNER TO ad_java;

CREATE IF NOT EXISTS INDEX idx_interview_participants_interview_id ON interview_participants(interview_id);
CREATE IF NOT EXISTS INDEX idx_interview_participants_participant_id ON interview_participants(participant_id);
CREATE IF NOT EXISTS INDEX idx_interview_participants_participant_type ON interview_participants(participant_type);
CREATE IF NOT EXISTS INDEX idx_interview_participants_role ON interview_participants(role);
CREATE IF NOT EXISTS INDEX idx_interview_participants_confirmed_attendance ON interview_participants(confirmed_attendance);
CREATE IF NOT EXISTS INDEX idx_interview_participants_attended ON interview_participants(attended);
CREATE IF NOT EXISTS INDEX idx_interview_participants_created_at ON interview_participants(created_at);
-------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS interview_slots (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    interviewer_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    scheduled_by BIGINT,
    cancelled_by BIGINT,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
GRANT ALL PRIVILEGES ON TABLE interview_slots TO ad_java;
ALTER TABLE interview_slots OWNER TO ad_java;


-- Create indexes for better performance
CREATE IF NOT EXISTS INDEX idx_interview_slots_interview_id ON interview_slots(interview_id);
CREATE IF NOT EXISTS INDEX idx_interview_slots_interviewer_id ON interview_slots(interviewer_id);
CREATE IF NOT EXISTS INDEX idx_interview_slots_start_time ON interview_slots(start_time);
CREATE IF NOT EXISTS INDEX idx_interview_slots_end_time ON interview_slots(end_time);
CREATE IF NOT EXISTS INDEX idx_interview_slots_status ON interview_slots(status);
CREATE IF NOT EXISTS INDEX idx_interview_slots_scheduled_by ON interview_slots(scheduled_by);
CREATE IF NOT EXISTS INDEX idx_interview_slots_time_range ON interview_slots(start_time, end_time);
CREATE IF NOT EXISTS INDEX idx_interview_slots_created_at ON interview_slots(created_at);
----------------------------------------------------------------------------------------------

