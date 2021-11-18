/* Section 1, Chapter 5 - Exercises 1 - 9 */

-- exercise 1
INSERT INTO terms
	(terms_id, terms_description, terms_due_days) -- column list can be in any seq
VALUES
	(6, "Net due 120 days", 120); -- values must be the same seq as list above
SELECT * FROM terms; -- check if table is updated

-- exercise 2
UPDATE terms -- update table
SET terms_description = "Net due 125 days", -- set column name with new value
	terms_due_days = 125
WHERE terms_id = 6; -- condition for an update to occur
SELECT * FROM terms; -- check if table is updated

-- exercise 3
DELETE FROM terms -- "delete from" is necessary and specify the table afterwards
WHERE terms_id = 6; -- condition for row to be deleted
SELECT * FROM terms; -- check if table is updated

-- exercise 4
INSERT INTO invoices
VALUES
	(DEFAULT, 32, "AX-014-027", '2018-08-1' , 434.58, 0.00, 0.00,
    2, '2018-08-31', NULL); -- column values must match order in column definitions
SELECT * FROM invoices; -- check if table updated

-- exercise 5
SELECT invoice_id FROM invoices -- check the last value added from exercise 4
ORDER BY invoice_id;

INSERT INTO invoice_line_items
	 (invoice_id, invoice_sequence, account_number, line_item_amount,  -- column list
     line_item_description)
VALUES
	   (122, 1, 160, 180.23, "Hard drive"),  -- update values in table with invoice_id from invoices
	   (122, 2, 527, 254.35, "Exchange Server update");
SELECT * FROM invoice_line_items; -- check if table updated


-- exercise 6
UPDATE invoices
SET credit_total = 0.10 * invoice_total,
    payment_total = invoice_total - credit_total -- get diff between invoice_total and credit_total to find the value for payment_total
WHERE invoice_id = 122;
SELECT * FROM invoices; -- check if table updated

-- exercise 7
UPDATE vendors 
SET default_account_number = 403 -- SET value based on condition below
WHERE vendor_id = 44; -- condition for the update

SELECT * FROM vendors -- check results 
WHERE vendor_id = 44;

-- exercise 8
UPDATE invoices 
SET terms_id = 2 -- update based on WHERE condition
WHERE vendor_id IN -- all invoices with the vendor_id IN vendors with a default_terms_id of 2
	(SELECT vendor_id 
    FROM vendors
    WHERE default_terms_id = 2);

SELECT terms_id, vendor_id FROM invoices -- check result table
WHERE terms_id = 2;

-- exercise 9 
DELETE FROM invoice_line_items -- invoice_line_items added from invoices so this needs to be deleted first 
WHERE invoice_id = 122; 
DELETE FROM invoices 
WHERE invoice_id = 122;

-- check result from invoices
SELECT invoice_id FROM invoices  
ORDER BY invoice_id DESC;

-- check result from invoice_line_items
SELECT invoice_id FROM invoice_line_items 
ORDER BY invoice_id DESC;