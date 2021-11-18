/* Section 3, Chapter 11 - Exercises 1 - 5 */

-- exercise 1
USE ap; -- use ap databases
CREATE INDEX vendors_vendor_zip_code_ix -- create index name here
ON vendors (vendor_zip_code); -- ON "table" with specified column in ()


-- exercise 2
 -- DROP TABLES FOR DEMO
DROP TABLE ex.members_committees, ex.committees, ex.members; 

CREATE TABLE ex.members  -- create table in ex database 

(-- name       type         column definitions (primary key usually goes after type)
member_id		INT			AUTO_INCREMENT,
first_name	VARCHAR(30)		NOT NULL,	
last_name	VARCHAR(30)		NOT NULL,
address		VARCHAR(40)		NOT NULL,
city		VARCHAR(25)		NOT NULL,
state		CHAR(2)			NOT NULL,
phone		VARCHAR(15)		NOT NULL,
CONSTRAINT members_pk PRIMARY KEY (member_id)	-- constraint with primary key
);

CREATE TABLE ex.committees
(
committee_id		INT				AUTO_INCREMENT,
committee_name 		VARCHAR(40)		NOT NULL,

CONSTRAINT committees_pk PRIMARY KEY (committee_id)
);

CREATE TABLE ex.members_committees
(
member_id		INT,
committee_id	INT,

CONSTRAINT members_committees_fk_members FOREIGN KEY (member_id)
REFERENCES members (member_id), -- foreign key from members table & member_id attribute column
    
CONSTRAINT members_committes_fk_committees FOREIGN KEY (committee_id)
REFERENCES committees (committee_id) -- foreign key from committees table & committee attribute column
);

-- exercise 3
USE ex; -- use ex database
INSERT INTO members
VALUES
	(DEFAULT, "Test_First", "Test_Last", "Test_Address",
    "Test_City", "CA", "777-777-7777"),  -- row 1
    (DEFAULT, "Test_First2", "Test_Last2", "Test_Address2",
    "Test_City2", "CA", "777-777-7777"); -- row 2
SELECT * FROM ex.members; -- check if table is updated

INSERT INTO committees
VALUES
	(DEFAULT, "Name1"),
    (DEFAULT, "Name2");
SELECT * FROM committees; -- check if table is updated

INSERT INTO members_committees
VALUES (1, 2),
	   (2, 1),
       (2, 2);
SELECT * FROM members_committees; -- check if table is updated

SELECT committee_name, last_name, first_name
FROM members m JOIN members_committees mc
	ON m.member_id = mc.member_id
JOIN committees c
    ON c.committee_id = mc.committee_id
    
ORDER BY committee_name, last_name, first_name;

-- exercise 4
ALTER TABLE members

/*the left parameter of DECIMAL() is the TOTAL # of digits on the left 
and right of the decimal point, the second parameter 
is the number of places on the right of the decimal point */

ADD annual_dues DECIMAL(5,2) NOT NULL DEFAULT 52.50, -- 3 decimal points to the left and 2 to the right
ADD	payment_date DATE; -- date type 
SELECT * FROM members;

-- exercise 5
ALTER TABLE committees 
MODIFY committee_name VARCHAR(40) NOT NULL UNIQUE; -- keep same length but change data type

/* this query checks the column "committee_name" 
in the "ex" database and returns the data types */

SELECT COLUMN_KEY -- column key is where unique would be 
FROM information_schema.COLUMNS
WHERE table_schema = 'ex' AND column_name = 'committee_name';

INSERT INTO committee
VALUES
	(DEFAULT, "Name1");