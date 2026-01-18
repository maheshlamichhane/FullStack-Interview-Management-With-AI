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
SELECT * FROM employee_practise WHERE last_name IS NULL;
SELECT * FROM employee_practise WHERE last_name IS NOT NULL;

-- DELETE OPERATIONS
DELETE FROM employee_practise WHERE id=3;
DELETE FROM employee_practise WHERE id=3 RETURNING *;

-- DROP OEPRATIONS
DROP TABLE employee_practise;
DROP TABLE address_practise;



--- DATA TYPES----------------------------------------------------------------------

-- -- TRUE FALSE,'true' 'false','t' 'f','y' 'n','yes' 'no','1' '0'
--  Character Data types char,varchar,text
-- Numbers data types Integers(smallint,integer,bigint) and Floating point
-- Auto increament integer data type:SERIAL(smallserial,serial,bigserial)
-- Date/Time data types Date,Time,Timestamp,Timestamptz,Interval
-- UUID data type
-- Array data types
-- hstore data type store data in key pair
-- JSON data type
-- Network Address Data Type like cidr,inet,macaddr,macaddr8,inet
---------------------------------------------------------------------

-- MODIFY DB STRUCTURE
ALTER TABLE address_practise ADD COLUMN test_column VARCHAR(50);
ALTER TABLE address_practise RENAME COLUMN test_column TO test_column2;
ALTER TABLE address_practise ALTER COLUMN test_column2 TYPE INT USING test_column2::integer;
ALTER TABLE address_practise ALTER COLUMN test_column2 TYPE BIGINT;
ALTER TABLE address_practise ALTER COLUMN test_column2 SET DEFAULT 10;
ALTER TABLE address_practise RENAME TO address_practise2;
ALTER TABLE address_practise DROP COLUMN test_column2;
ALTER TABLE address_practise ADD CONSTRAINT unique_country UNIQUE (country);
ALTER TABLE address_practise ADD CHECK (country IN ('NEPAL','SAUDI'));
--------------------------------------------------------------------------------------

------ DATA TYPE CONVERSION Type conversion Implicit,Explicit via CAST
-- No Conversion
SELECT * FROM address_practise WHERE id=1;

-- Automatic Conversion
SELECT * FROM address_practise WHERE id='1';

-- Explicit Conversion
SELECT * FROM address_practise WHERE id= integer '1';

-- Using CAST FOR Conversion
SELECT CAST('10' AS INTEGER);
SELECT CAST('2020-01-01' AS DATE),CAST('01-MAY-2020' AS DATE);
SELECT CAST('true' AS BOOLEAN), CAST('false' as BOOLEAN), CAST('T' as BOOLEAN),
CAST('F' as BOOLEAN);
SELECT CAST('14.7888' AS DOUBLE PRECISION);
SELECT '10'::INTEGER,'2020-01-01'::DATE;
SELECT '2020-02-20 10:30:25.467'::TIMESTAMP;
SELECT '2020-02-20 10:30:25.467'::TIMESTAMPTZ;
SELECT ROUND(10,4) AS "result";
SELECT SUBSTR('123456',2) AS "result";

-----------------------------------------------------------------------------------
--- CONVERSION FUNCTION

-- Convert an integer to a string
SELECT TO_CHAR(100870,'9,99999');

-- lets view movie release dae in DD-MM-YYYY format
SELECT date_of_birth,TO_CHAR(date_of_birth,'DD-MM-YYYY'),
TO_CHAR(date_of_birth,'Dy, MM, YYYY') FROM employee_practise;

-- converting timestamp literal to a string
SELECT TO_CHAR(TIMESTAMP '2020-01-01 10:30:5','HH24:MI:SS');

-- Adding currency symbol to say movies revenues
SELECT TO_CHAR(age,'$99999D99')  FROM employee_practise;

-- Convert a string to a number
SELECT TO_NUMBER('1420.88','9999.');

-- Formating
SELECT TO_NUMBER('$1,420.64','L9G999.99');
SELECT TO_NUMBER('1,234,567.87','9G999g999D99');

-- String to date
SELECT TO_DATE('2020/10/22','YYYY/MM/DD');
SELECT TO_DATE('022188','MMDDYY');
SELECT TO_DATE('March 07, 2018', 'Month DD, YYYY');

-- String to timestamp
SELECT TO_TIMESTAMP('2020-10-28 10:30:25','YYYY-MM-DD HH:MI:SS');
SELECT TO_TIMESTAMP('2020-01-01 23:08:00','YYYY-MM-DD HH24:MI:SS');

-----------------------------------------------------------------------------------

-- USER DEFINED FUNCTIONS

-- Create addr user defined data type
CREATE DOMAIN addr VARCHAR(100) NOT NULL;
CREATE DOMAIN positive_numeric INT NOT NULL CHECK (VALUE > 0);
CREATE DOMAIN us_postal_code AS TEXT CHECK(VALUE ~'^\d{5}$' OR VALUE ~'^\D{5}-\d{4}$');
CREATE DOMAIN proper_email VARCHAR(150)  
CHECK (VALUE ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$');
CREATE DOMAIN valid_color VARCHAR(10) CHECK (VALUE IN ('red','green','blue'))
CREATE TYPE currency AS ENUM ('USD','EUR','GBP');

-----------------------------------------------------------------------------------

-- CONSTRAINTS
-- NOT NULL constraints
-- UNIQUE constraints
-- Default constraints
-- Primary Key Constraint on single column
-- Foreign Key Constraint
 -- CHECK constraint


 -----------------------------------------------------------------------------------
 -- SEQUENCE
 
-- Create a sequence
CREATE SEQUENCE IF NOT EXISTS test_seq;
SELECT nextval('test_seq');
SELECT currval('test_seq');
SELECT setval('test_seq',100);
SELECT setval('test_seq',200,false);
CREATE SEQUENCE IF NOT EXISTS test_seq2 START WITH 100;
CREATE SEQUENCE IF NOT EXISTS test_seq3 
INCREMENT 50 MINVALUE 400 MAXVALUE 6000 START WITH 500;
SELECT nextval('test_seq3');

-- Alter a sequence
SELECT nextval('test_seq');
ALTER SEQUENCE test_seq RESTART WITH 100;
ALTER SEQUENCE test_seq RENAME TO my_sequence4;

-- Specify data type for sequence
CREATE SEQUENCE IF NOT EXISTS test_seq_smallint AS SMALLINT
CREATE SEQUENCE IF NOT EXISTS test_seq_smallint AS INT

-- Create a descending sequence and CYCLE | NO CYCLE
CREATE SEQUENCE seq_des INCREMENT -1 MINVALUE 1 MAXVALUE 3 START 3 CYCLE;
CREATE SEQUENCE seq_des2 INCREMENT -1 MINVALUE 1 MAXVALUE 3 START 3 NO CYCLE;

-- Share sequence among multiple tables
CREATE SEQUENCE common_fruits_seq START WITH 100;
CREATE TABLE apples(
	fruit_id INT DEFAULT nextval('common_fruits_seq') NOT NULL,
	fruit_name VARCHAR(50)
);
 
 ----------------------------------------------------------------------------------------
-- STRING FUNCTIONS
SELECT UPPER ('amazing postgresql');
SELECT LOWER('Amazing Postgresql');
SELECT INITCAP('the world is changing with a lighting speed');
SELECT LEFT('ABCD',1);
SELECT LEFT('ABC',-2);
SELECT RIGHT('ABCD',2);
SELECT RIGHT('ABCD',-1);
SELECT REVERSE('Amazing PostgreSQL');
SELECT SPLIT_PART('ONE,TWO,THREE',',',2);
SELECT SPLIT_PART('A|B|C|D','|',3);
SELECT
TRIM(LEADING FROM ' Amazing PostgreSQL'),
TRIM(TRAILING FROM 'Amazing PostgreSQL '),
TRIM(' Amazing PostgreSQL ');
SELECT LTRIM('yummy','y');
SELECT RTRIM('yummy','y');
SELECT BTRIM('yummy','y');
SELECT LTRIM(' Amazing PostgeSQL');
SELECT BTRIM(' Amazing PostgeSQL ');
SELECT RTRIM('Amazing PostgeSQL ');
SELECT LPAD('Database',15,'*');
SELECT RPAD('Database',15,'*');
SELECT LENGTH('Amazing PostgreSQL');
SELECT POSITION('Amazing' IN 'Amazing PostgreSQL');
SELECT strpos('World Bank','bank');
SELECT substring('What a wonderful world' from 1 for 4);
SELECT repeat('A',4);
SELECT REPLACE('ABC XYZ','X','1');
SELECT replace('I like cats','cats','dogs');

 ----------------------------------------------------------------------------------------
 -- Aggregate Functions
 SELECT count(*) FROM address_practise;
 SELECT SUM(age) FROM employee_practise;
 SELECT MAX(age) FROM employee_practise;
 SELECT MIN(age) FROM employee_practise;
 SELECT GREATEST(200,20,30);
 SELECT LEAST(-10,3,100);
 SELECT GREATEST('A','B','C');
 SELECT LEAST('A','B','C');
 SELECT AVG(age) from employee_practise;

  ----------------------------------------------------------------------------------------
-- DATE TIME FUNCTIONS

-- To view current setting for date style
SHOW DateStyle;

-- To change date style you can use 
SET DateStyle = 'ISO,DMY';

-- Using TO_DATE function
SELECT TO_DATE('2025-11-08','YYYY-MM-DD');
SELECT TO_DATE('20251108','YYYYMMDD');
SELECT TO_DATE('08-11-2024','DD-MM-YYYY');
SELECT TO_DATE('December 1, 2020','Month DD, YYYY');
SELECT TO_DATE('Dec 1, 2020','Mon DD, YYYY');
SELECT TO_DATE('8th Dec, 2020', 'DDth Mon, YYYY');

-- Using TO_TIMESTAMP function
SELECT TO_TIMESTAMP('2020-02-02 10:30:20','YYYY-MM-DD HH:MI:SS');
SELECT TO_TIMESTAMP('2020-02-02 10:30:20','YYYY-MM-DD HH:MI');
SELECT TO_TIMESTAMP('2020-02-02 10:30:20','YYYY-MM-DD HH');
SELECT TO_TIMESTAMP('2020-02-02 20:30:20','YYYY-MM-DD HH24:MI:SS');
SELECT TO_TIMESTAMP('2020-02-02 10:4','YYYY-MM-DD SS:MS');

-- Date construction functions
SELECT MAKE_DATE(2020,01,01);
SELECT MAKE_TIME(2,3,4.05);
SELECT MAKE_TIMESTAMP(2020,1,1,10,30,45);
SELECT MAKE_INTERVAL(2020,01,01,01,10,30,45);
SELECT MAKE_INTERVAL(days => 10);
SELECT MAKE_INTERVAL(months => 10, days => 2,mins => 35);
SELECT MAKE_INTERVAL(weeks => 2);
SELECT MAKE_TIMESTAMPTZ(2020,02,15,10,35,15.35);

-- AGE function
SELECT age('2020-01-01','2018-10-01');
SELECT age( timestamp '2018-08-01');
SELECT age(CURRENT_DATE, TIMESTAMP '2020-01-01');

  ----------------------------------------------------------------------------------------
-- GROUPING DATA
-- FROM -> WHERE -> GROUP BY -> HAVING -> SELECT -> DISTINCT -> ORDER BY -> LIMIT
SELECT SUM(age) FROM employee_practise GROUP BY gender;
SELECT SUM(age) FROM employee_practise GROUP BY gender HAVING SUM(age) > 100;

  ----------------------------------------------------------------------------------------
  -- JOINS
 SELECT * FROM employee_practise INNER JOIN address_practise 
 ON employee_practise.id = address_practise.employee_id;

 SELECT * FROM employee_practise LEFT JOIN address_practise
 ON employee_practise.id = address_practise.employee_id;

 SELECT * FROM employee_practise RIGHT JOIN address_practise
 ON employee_practise.id = address_practise.employee_id;

 SELECT * FROM employee_practise FULL OUTER JOIN address_practise
 ON employee_practise.id = address_practise.employee_id;

 SELECT * FROM employee_practise CROSS JOIN address_practise;

 SELECT * FROM employee_practise NATURAL JOIN address_practise;

 ----------------------------------------------------------------------------------------
 -- COMBINING QUERY TOGETHER

 -- Combining queries together with UNION
 SELECT 
	product_id,product_name
FROM left_products
UNION
SELECT
	product_id,product_name
FROM right_products;

-- Combining queries together with INTERSECT
SELECT 
	product_id,
	product_name
FROM left_products
INTERSECT
SELECT
	product_id,
	product_name
FROM right_products;

-- Combining queries together with EXCEPT
SELECT
	product_id,
	product_name
FROM left_products
EXCEPT
SELECT
	product_id,
	product_name
FROM right_products

----------------------------------------------------------------------------------------

 -- SCHEMAS
 CREATE SCHEMA sales;
 ALTER SCHEMA sales RENAME TO programming;
 DROP SCHEMA programming;
 SELECT * FROM hr.public.employees;
 SELECT current_schema();
 ALTER SCHEMA humanresources OWNER TO adam;
 GRANT USAGE ON SCHEMA private TO adam;
GRANT SELECT ON ALL TABLES IN SCHEMA private TO adam;

 ----------------------------------------------------------------------------------------

-- ARRAY FUNCTIONS
SELECT
	INT4RANGE(1,6) AS "Default [( = closed - opened",
	NUMRANGE(1.4213,6.286,'[]') AS "[] closed - closed",
	DATERANGE('20100101','20201220','()') AS "Dates () = opened - opened",
	TSRANGE(LOCALTIMESTAMP, LOCALTIMESTAMP + INTERVAL '8 DAYS', '(]')
	AS "opened-closed";

	SELECT 
	ARRAY[1,2,3] AS "INT arrays",
	ARRAY[2.12225::float] AS "floating numbers with putting explicit typing",
	ARRAY[CURRENT_DATE,CURRENT_DATE + 5];

SELECT
	ARRAY[1,2,3,4] = ARRAY[1,2,3,4] AS "Equality",
	ARRAY[1,2,3,4] = ARRAY[1,2,3] AS "Equality",
	ARRAY[1,2,3,4] <> ARRAY[2,3,4,5] AS "Not Equal to",
	ARRAY[1,2,3,4] < ARRAY[2,3,4,5] AS "Less than",
	ARRAY[1,2,3,4] <= ARRAY[2,3,4,5] AS "Less than and equal to",
	ARRAY[1,2,3,4] > ARRAY[2,3,4,5] AS "Greater than",
	ARRAY[1,2,3,4] >= ARRAY[2,3,4,5] AS "Greater than and equal to";

SELECT
	ARRAY[1,2,3,4] @> ARRAY[2,3,4] AS "Contains",
	ARRAY['a','b'] <@ ARRAY['a1','b1','c'] AS "Contained by",
	ARRAY[1,2,3,4] && ARRAY[21,31,41] AS "Is overlap";


 ----------------------------------------------------------------------------------------
 -- INDEXES AND PERFORMANCE OPTIMIZATION 
 
 -- Lets create an index on order date on orders table 
CREATE INDEX idx_employee_practise_date_of_birth ON employee_practise (date_of_birth);

-- Create an index on multiple fields say orders -> customer_id,order_id
CREATE INDEX idx_orders_customer_id_order_id ON orders (customer_id,order_id);

-- Lets create a UNIQUE index on products table on product_id
CREATE UNIQUE INDEX idx_u_products_product_id ON products (product_id);
CREATE UNIQUE INDEX idx_u_orders_order_id_customer_id 
ON orders (order_id,customer_id);

-- Sequential Scan
EXPLAIN SELECT * FROM employee_practise;
EXPLAIN SELECT * FROM employee_practise WHERE id IS NOT NULL;

-- Index Scan
EXPLAIN SELECT * FROM employee_practise WHERE id = 1;

 -- List counts fro all indexes
-- all stats
SELECT 
*
FROM
	pg_stat_all_indexes;

DROP INDEX idx_suppliers_region;


 
 
 

  

SELECT * FROM employee_practise;









