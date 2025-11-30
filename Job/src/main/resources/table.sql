CREATE TABLE departments (
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
CREATE INDEX idx_departments_parent_id ON departments(parent_department_id);
CREATE INDEX idx_departments_manager_id ON departments(manager_id);
CREATE INDEX idx_departments_is_active ON departments(is_active);
CREATE INDEX idx_departments_created_at ON departments(created_at);
CREATE INDEX idx_departments_code ON departments(code);
-----------------------------------------------------------------------------------------------

