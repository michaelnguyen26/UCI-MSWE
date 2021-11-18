/* Section 2, Chapter 6 - Exercises 1 - 8 */ 

-- exercise 1
USE ap; -- use ap database 

SELECT vendor_id AS number_of_vendors,
SUM(invoice_total) AS vendor_sum_invoice_total -- sum the invoice total for each vendor
FROM invoices
GROUP BY vendor_id -- group by vendor_id will result in grouping the vendor_id to their respective total sum
ORDER BY vendor_id; 

-- exercise 2
SELECT vendor_name, SUM(payment_total) AS vendor_payment_total -- get vendor_name assigned with their total sum
FROM vendors v JOIN invoices i -- join invoices to see each vendors total invoice 
	ON v.vendor_id = i.vendor_id -- primary and foreign key relationship
GROUP BY vendor_name -- condense multiple vendor_names into one associated with their total sum of payment
ORDER BY vendor_payment_total DESC; -- check the highest payment total by ordering by DESC

-- exercise 3
SELECT vendor_name, COUNT(*) AS vendor_invoice_count, -- count the total rows in the vendor_name group
SUM(invoice_total) AS invoice_total_sum -- sum of each vendor invoice
FROM vendors v JOIN invoices i
	ON v.vendor_id = i.vendor_id
GROUP BY vendor_name -- condense each multiple vendor_name into one and calculate results
ORDER BY vendor_invoice_count DESC; -- highest invoice count first

-- exercise 4
SELECT account_description, COUNT(*) AS "Line Item Count", -- select only these columns and return the rows based on the GROUP BY clause
SUM(line_item_amount) AS sum_of_line_item
FROM general_ledger_accounts gl JOIN invoice_line_items li
	ON gl.account_number = li.account_number
GROUP BY account_description
HAVING COUNT(*) > 1 -- applies after grouping and limits the group to counts > 1 (only HAVING works with aggregates)
ORDER BY sum_of_line_item DESC;

-- exercise 5 
SELECT account_description, COUNT(*) AS "Line Item Count", -- select only these columns and return the rows based on the GROUP BY clause
SUM(line_item_amount) AS sum_of_line_item, invoice_date
FROM general_ledger_accounts gl JOIN invoice_line_items li
	ON gl.account_number = li.account_number -- pk and fk relationship
JOIN invoices i -- join invoices to get the invoice date
	ON li.invoice_id = i.invoice_id -- second pk and fk relationship
WHERE invoice_date BETWEEN '2018-04-01' AND '2018-06-30' -- date must be inclusive between these two dates 
GROUP BY account_description
HAVING COUNT(*) > 1 -- applies after grouping (only HAVING works with aggregates)
ORDER BY sum_of_line_item DESC;

-- exercise 6
SELECT li.account_number, SUM(line_item_amount) AS sum_of_line_item_amount
FROM invoice_line_items li JOIN general_ledger_accounts gl
	ON li.account_number = gl.account_number -- pk and fk relationship 
GROUP BY li.account_number WITH ROLLUP; -- add summary at the end of the result set (total for the entire column of sums)

-- exercise 7 
SELECT vendor_name, COUNT(DISTINCT li.account_number) AS gl_accounts -- li has a fk account_number
FROM vendors v JOIN invoices i
    ON v.vendor_id = i.vendor_id -- relationship between vendor and invoice
JOIN invoice_line_items li
    ON i.invoice_id = li.invoice_id -- relationship between invoice and line items
GROUP BY vendor_name -- group by finding which vendors
HAVING gl_accounts > 1; -- find more than one account

-- exercise 8
SELECT terms_id, vendor_id, 
MAX(payment_date) AS max_payment_date, -- last payment date
SUM(invoice_total - credit_total - payment_total) AS balance_due
FROM invoices
GROUP BY terms_id, vendor_id WITH ROLLUP;

/* WITH ROLLUP only performs on aggregate columns, so terms_id and vendor_id need a GROUPING function */

SELECT IF(GROUPING(terms_id) = 1, 'Total', terms_id) AS terms_id, -- if true, second expression, otherwise third expression
IF(GROUPING(vendor_id) = 1, 'Total for Terms ID', vendor_id) AS vendor_id,
MAX(payment_date) AS max_payment_date,
SUM(invoice_total - credit_total - payment_total) AS balance_due
FROM invoices
GROUP BY terms_id, vendor_id WITH ROLLUP;