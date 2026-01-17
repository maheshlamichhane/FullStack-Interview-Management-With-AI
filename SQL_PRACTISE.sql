-- CREATING TABLE -- 
CREATE TABLE employee_practise(
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50),
	email VARCHAR(50) NOT NULL UNIQUE,
	date_of_birth DATE NOT NULL,
	age INT NOT NULL,
	salary NUMERIC(10,2) NOT NULL,
	gender CHAR(1) NOT NULL,
	created_at DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATE NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE address_practise(
	id SERIAL PRIMARY KEY,
	country VARCHAR(50) NOT NULL,
	district VARCHAR(20) NOT NULL,
	employee_id INT NOT NULL,
	CONSTRAINT fk_empoyee FOREIGN KEY(employee_id) REFERENCES employee_practise(id)
);

-- INSERT OPERATIONS
INSERT INTO employee_practise VALUES(1,'Mahesh','Lamichhane','mahesh@gmail.com','1994-11-09',
31,120000,'M');
INSERT INTO employee_practise VALUES(2,'Akash',null,'akash@gmail.com','1993-07-22',
32,150000,'M');
INSERT INTO employee_practise(id,first_name,email,date_of_birth,age,salary,gender) 
VALUES(3,'Pawan','pawan@gmail.com','2003-01-18',22,100000,'M');

INSERT INTO address_practise (id,country,district,employee_id) VALUES (1,'Nepal','Kathmandu',1);
INSERT INTO address_practise (id,country,district,employee_id) VALUES (2,'SAUDI','Baglung',2);

-- UPDATE OPERATIONS
UPDATE employee_practise SET email='mahesh048@gmail.com' WHERE id=1;
UPDATE employee_practise SET salary=80000 WHERE id=3 RETURNING *;
UPDATE employee_practise SET date_of_birth='1993-06-18' where id=2;


-- FETCH OPERATIONS
SELECT * FROM employee_practise;
SELECT id,first_name,last_name FROM employee_practise;
SELECT * FROM employee_practise WHERE id=2;
SELECT * FROM employee_practise WHERE email like '%@gmail.com';
SELECT first_name as firstName,last_name as lastName FROM employee_practise;
SELECT first_name || ' ' || last_name AS "Full Name" FROM employee_practise;
SELECT * FROM employee_practise ORDER BY date_of_birth ASC;
SELECT * FROM employee_practise ORDER BY date_of_birth DESC;
SELECT * FROM employee_practise ORDER BY date_of_birth DESC, email ASC;
SELECT * FROM employee_practise ORDER BY last_name NULLS FIRST;
SELECT * FROM employee_practise ORDER BY last_name NULLS LAST;
SELECT first_name,LENGTH(last_name) FROM employee_practise;
SELECT * FROM employee_practise ORDER BY 1 DESC;
SELECT * FROM employee_practise WHERE first_name LIKE '%h' AND age >= 32;
SELECT * FROM employee_practise WHERE date_of_birth='1994-11-09' OR first_name='Pawan';
SELECT * FROM employee_practise WHERE salary <> 100000;
SELECT * FROM employee_practise ORDER BY id DESC LIMIT 2;
SELECT * FROM employee_practise WHERE date_of_birth='1994-11-09' ORDER BY date_of_birth
LIMIT 1;
SELECT * FROM employee_practise WHERE date_of_birth='1994-11-09' ORDER BY date_of_birth
FETCH FIRST 1 ROW ONLY;
SELECT * FROM employee_practise WHERE age IN (22,32);
SELECT * FROM employee_practise WHERE age NOT IN (22,32);
SELECT * FROM employee_practise WHERE date_of_birth BETWEEN '1993-01-01' AND '1995-01-01';
SELECT * FROM employee_practise WHERE date_of_birth NOT BETWEEN '1993-01-01' AND '1995-01-01';
SELECT * FROM employee_practise WHERE first_name like '%h';
SELECT * FROM employee_practise WHERE first_name like 'P%';
SELECT * FROM employee_practise WHERE first_name like '%e%';
SELECT * FROM employee_practise WHERE first_name like '_a%';
SELECT * FROM employee_practise WHERE first_name like '__w%';




-- DELETE OPERATIONS
DELETE FROM employee_practise WHERE id=3;
DELETE FROM employee_practise WHERE id=3 RETURNING *;

-- DROP OEPRATIONS
DROP TABLE employee_practise;
DROP TABLE address_practise;