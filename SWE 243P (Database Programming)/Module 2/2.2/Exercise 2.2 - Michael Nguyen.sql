/* Section 1, Chapter 4 - Exercises 1 - 7 */

-- exercise 1
SELECT * FROM vendors v JOIN invoices i
ON v.vendor_id = i.vendor_id; -- join on primary and foreign key (common row data between tables)

-- exercise 2
SELECT vendor_name, invoice_number, invoice_date,
invoice_total - (payment_total + credit_total) AS balance_due
FROM vendors v JOIN invoices i
ON v.vendor_id = i.vendor_id  -- join on common row data between tables
WHERE invoice_total - (payment_total + credit_total) > 0
ORDER BY vendor_name; -- sort in ASC default 

-- exercise 3
SELECT vendor_name, default_account_number AS default_account,
account_description AS "description"
FROM vendors v JOIN general_ledger_accounts g
ON v.default_account_number = g.account_number -- join on common row data between tables
ORDER BY account_description, vendor_name;

-- exercise 4
SELECT vendor_name, invoice_date, invoice_number,
invoice_sequence AS li_sequence, line_item_amount AS li_amount
FROM vendors v 
	JOIN invoices i -- join the first table
		ON v.vendor_id = i.vendor_id  -- join on common row data between tables
    JOIN invoice_line_items li -- join second table 
		ON i.invoice_id = li.invoice_id  -- join on common row data between tables
ORDER BY vendor_name, invoice_date, invoice_number,
invoice_sequence;

-- exercise 5
SELECT v1.vendor_id, v2.vendor_name, -- identify which rows to select from the same table
CONCAT(v1.vendor_contact_first_name, " ", v2.vendor_contact_last_name) AS contact_name
FROM vendors v1 JOIN vendors v2 -- self join
ON v1.vendor_contact_last_name = v2.vendor_contact_last_name -- self join vendor_last_name's are equal
WHERE v1.vendor_id <> v2.vendor_id  -- self join vendor_id's are not equal
ORDER BY v1.vendor_contact_last_name;

-- exercise 6
SELECT gl.account_number, gl.account_description
FROM general_ledger_accounts gl 
	LEFT JOIN invoice_line_items li -- outer join "gl" to include all the rows and only mismatched rows from "li"
		ON li.account_number = gl.account_number
WHERE li.invoice_id IS NULL
ORDER BY gl.account_number;

-- exercise 7
	SELECT "CA" AS vendor_state, vendor_name -- select data as vendor_state that is in CA
	FROM vendors 
	WHERE vendor_state = 'CA'
UNION -- join two select statements (result sets) into one table
	SELECT "Outside CA" AS vendor_state, vendor_name -- select data as vendor_state that is not in CA
    FROM vendors 
    WHERE vendor_state <> 'CA'
ORDER BY vendor_name;
