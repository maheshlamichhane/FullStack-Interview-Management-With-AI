CREATE TABLE candidates (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    linkedin_url VARCHAR(500),
    github_url VARCHAR(500),
    portfolio_url VARCHAR(500),
    current_company VARCHAR(255),
    current_position VARCHAR(255),
    total_experience NUMERIC(4,1), -- 999.9 years maximum
    current_salary NUMERIC(12,2), -- Up to 999,999,999.99
    expected_salary NUMERIC(12,2), -- Up to 999,999,999.99
    notice_period INTEGER, -- in days
    is_active BOOLEAN DEFAULT true,
    employment_status VARCHAR(50),
    preferred_location VARCHAR(255),
    current_location VARCHAR(255),
    willing_to_relocate BOOLEAN DEFAULT false,
    source VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for better query performance
CREATE INDEX idx_candidates_email ON candidates(email);
CREATE INDEX idx_candidates_is_active ON candidates(is_active);
CREATE INDEX idx_candidates_employment_status ON candidates(employment_status);
CREATE INDEX idx_candidates_created_at ON candidates(created_at);


GRANT ALL PRIVILEGES ON TABLE candidates TO ad_java;
ALTER TABLE candidates OWNER TO ad_java;
------------------------------------------------------------------------------------------------