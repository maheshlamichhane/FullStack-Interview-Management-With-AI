CREATE TABLE  IF NOT EXISTS  departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(100) UNIQUE,
    description TEXT,
    parent_department_id BIGINT,
    manager_id BIGINT,
    is_active BOOLEAN DEFAULT true,
    budget_code VARCHAR(100),
    cost_center VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Self-referencing foreign key for department hierarchy
    CONSTRAINT fk_department_parent
        FOREIGN KEY (parent_department_id)
        REFERENCES departments(id) ON DELETE SET NULL
);

GRANT ALL PRIVILEGES ON TABLE departments TO ad_java;
ALTER TABLE departments OWNER TO ad_java;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS  idx_departments_parent_id ON departments(parent_department_id);
CREATE INDEX IF NOT EXISTS  idx_departments_manager_id ON departments(manager_id);
CREATE INDEX IF NOT EXISTS  idx_departments_is_active ON departments(is_active);
CREATE INDEX IF NOT EXISTS  idx_departments_created_at ON departments(created_at);
CREATE INDEX IF NOT EXISTS  idx_departments_code ON departments(code);
-----------------------------------------------------------------------------------------------
-- Create job_positions table
CREATE TABLE IF NOT EXISTS  job_positions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    department_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    experience_level VARCHAR(50) NOT NULL,
    min_salary NUMERIC(12,2),
    max_salary NUMERIC(12,2),
    open_positions INTEGER DEFAULT 1,
    filled_positions INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'DRAFT',
    is_remote BOOLEAN DEFAULT false,
    application_deadline TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

GRANT ALL PRIVILEGES ON TABLE job_positions TO ad_java;
ALTER TABLE job_positions OWNER TO ad_java;

-- Create indexes
CREATE IF NOT EXISTS  INDEX idx_job_positions_department_id ON job_positions(department_id);
CREATE IF NOT EXISTS  INDEX idx_job_positions_location_id ON job_positions(location_id);
CREATE IF NOT EXISTS  INDEX idx_job_positions_status ON job_positions(status);
CREATE IF NOT EXISTS  INDEX idx_job_positions_code ON job_positions(code);
-----------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) UNIQUE,
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255) NOT NULL,
    postal_code VARCHAR(50),
    timezone VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    is_remote BOOLEAN DEFAULT FALSE,
    contact_person VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    facilities TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    -- NO job_positions field here - it's managed in the job_positions table
);

GRANT ALL PRIVILEGES ON TABLE locations TO ad_java;
ALTER TABLE locations OWNER TO ad_java;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_locations_name ON locations(name);
CREATE INDEX IF NOT EXISTS idx_locations_country ON locations(country);
CREATE INDEX IF NOT EXISTS idx_locations_city ON locations(city);
CREATE INDEX IF NOT EXISTS idx_locations_is_active ON locations(is_active);
CREATE INDEX IF NOT EXISTS idx_locations_is_remote ON locations(is_remote);
CREATE INDEX IF NOT EXISTS idx_locations_code ON locations(code);
----------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS job_requirements (
    id BIGSERIAL PRIMARY KEY,
    job_position_id BIGINT NOT NULL,
    requirement_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    is_mandatory BOOLEAN DEFAULT TRUE,
    priority INTEGER DEFAULT 1,

    -- Foreign key constraint
    FOREIGN KEY (job_position_id)
        REFERENCES job_positions(id)
        ON DELETE CASCADE
);
GRANT ALL PRIVILEGES ON TABLE job_requirements TO ad_java;
ALTER TABLE job_requirements OWNER TO ad_java;
-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_job_requirements_job_position_id ON job_requirements(job_position_id);
CREATE INDEX IF NOT EXISTS idx_job_requirements_requirement_type ON job_requirements(requirement_type);
CREATE INDEX IF NOT EXISTS idx_job_requirements_is_mandatory ON job_requirements(is_mandatory);
CREATE INDEX IF NOT EXISTS idx_job_requirements_priority ON job_requirements(priority);
------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS job_skills (
    id BIGSERIAL PRIMARY KEY,
    job_position_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    proficiency_level VARCHAR(50),
    is_mandatory BOOLEAN DEFAULT TRUE,
    min_experience_years DECIMAL(4,2),
    priority INTEGER DEFAULT 1,

    -- Foreign key constraint
    FOREIGN KEY (job_position_id)
        REFERENCES job_positions(id)
        ON DELETE CASCADE,

    -- Ensure each skill is unique per job position
    UNIQUE(job_position_id, skill_name)
);

GRANT ALL PRIVILEGES ON TABLE job_skills TO ad_java;
ALTER TABLE job_skills OWNER TO ad_java;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_job_skills_job_position_id ON job_skills(job_position_id);
CREATE INDEX IF NOT EXISTS idx_job_skills_skill_name ON job_skills(skill_name);
CREATE INDEX IF NOT EXISTS idx_job_skills_category ON job_skills(category);
CREATE INDEX IF NOT EXISTS idx_job_skills_proficiency_level ON job_skills(proficiency_level);
CREATE INDEX IF NOT EXISTS idx_job_skills_is_mandatory ON job_skills(is_mandatory);
CREATE INDEX IF NOT EXISTS idx_job_skills_priority ON job_skills(priority);
---------------------------------------------------------------------------------------------

