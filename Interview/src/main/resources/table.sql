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
    interviewer_id BIGINT NOT NULL,
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
    interview_id BIGINT
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
CREATE TABLE feedbacks (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    provided_by BIGINT NOT NULL,
    provided_for BIGINT NOT NULL,
    technical_skills_rating INTEGER CHECK (technical_skills_rating >= 1 AND technical_skills_rating <= 5),
    communication_skills_rating INTEGER CHECK (communication_skills_rating >= 1 AND communication_skills_rating <= 5),
    problem_solving_rating INTEGER CHECK (problem_solving_rating >= 1 AND problem_solving_rating <= 5),
    cultural_fit_rating INTEGER CHECK (cultural_fit_rating >= 1 AND cultural_fit_rating <= 5),
    overall_rating INTEGER CHECK (overall_rating >= 1 AND overall_rating <= 5),
    strengths TEXT,
    areas_for_improvement TEXT,
    comments TEXT,
    recommendation VARCHAR(50),
    is_final_feedback BOOLEAN DEFAULT FALSE,
    is_shared_with_candidate BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraint
    CONSTRAINT fk_feedback_interview
        FOREIGN KEY (interview_id)
        REFERENCES interviews(id)
        ON DELETE CASCADE,

    -- Additional constraints for data integrity
    CONSTRAINT chk_ratings_range CHECK (
        (technical_skills_rating IS NULL OR (technical_skills_rating BETWEEN 1 AND 5)) AND
        (communication_skills_rating IS NULL OR (communication_skills_rating BETWEEN 1 AND 5)) AND
        (problem_solving_rating IS NULL OR (problem_solving_rating BETWEEN 1 AND 5)) AND
        (cultural_fit_rating IS NULL OR (cultural_fit_rating BETWEEN 1 AND 5)) AND
        (overall_rating IS NULL OR (overall_rating BETWEEN 1 AND 5))
    )
);

-- Create indexes for better performance
CREATE INDEX idx_feedback_interview_id ON feedbacks(interview_id);
CREATE INDEX idx_feedback_provided_by ON feedbacks(provided_by);
CREATE INDEX idx_feedback_provided_for ON feedbacks(provided_for);
CREATE INDEX idx_feedback_created_at ON feedbacks(created_at);

GRANT ALL PRIVILEGES ON TABLE feedbacks TO ad_java;
ALTER TABLE feedbacks OWNER TO ad_java;
----------------------------------------------------------------------------------------------
