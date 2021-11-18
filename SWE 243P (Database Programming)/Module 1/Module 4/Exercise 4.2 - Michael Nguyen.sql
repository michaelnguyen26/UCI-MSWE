/* Section 2, Chapter 7 - Exercises 1 - 7 */

-- exercise 1
USE ap;

-- SELECT DISTINCT vendor_name 
-- FROM vendors JOIN invoices 
-- ON vendors.vendor_id= invoices.vendor_id 
-- ORDER BY vendor_name;

SELECT DISTINCT vendor_name
FROM vendors
WHERE vendor_id IN -- subquery begins here
	(SELECT vendor_id
    FROM invoices)  -- get vendor_id from invoices as a list
ORDER BY vendor_name;

-- exercise 2
SELECT payment_total
FROM invoices 
WHERE payment_total > -- this payment total must be > than the averages of payment totals that are only greater than 0 
	(SELECT AVG(payment_total) -- get averages of payment totals that are only greater than 0 
	FROM invoices
	WHERE payment_total > 0)
ORDER BY invoice_total DESC; -- go from largest to smallest 

-- exercise 3
SELECT account_number, account_description 
FROM general_ledger_accounts gl
WHERE NOT EXISTS -- select account_number columns that do not exist using li.account_number = gl.account_number as a reference of those that do exist
	(SELECT * -- doesn't matter what columns, so usually a * is used
     FROM invoice_line_items li
     WHERE li.account_number = gl.account_number)
ORDER BY account_number;

-- exercise 4
SELECT vendor_name, i.invoice_id, invoice_sequence, 
line_item_amount
FROM vendors v JOIN invoices i
  ON v.vendor_id = i.vendor_id -- v and i have commone attributes (fk and pk)
JOIN invoice_line_items li
  ON i.invoice_id = li.invoice_id -- i and li have common attributes (fk and pk)
WHERE i.invoice_id IN -- search based on if the invoice_id is in a invoice_id list that has invoice_sequence's greater than 1 
      (SELECT DISTINCT invoice_id -- get invoice_id's only where the invoice_sequence is greater than 1
       FROM invoice_line_items               
       WHERE invoice_sequence > 1)
ORDER BY vendor_name, i.invoice_id, invoice_sequence;

-- exercise 5
SELECT vendor_id, MAX(invoice_total) AS invoice_total_max -- max gives the largest unpaid invoice
FROM invoices
WHERE invoice_total - credit_total - payment_total > 0 -- check if there is still an invoice total
GROUP BY vendor_id; -- condense each vendor_id to one group and apply each aggregate calculation to each group 

SELECT SUM(invoice_total_max) AS "Sum of Largest Unpaid Invoices" -- use the invoice_total_max below to sum the entire unpaid invoice from all vendors
FROM (SELECT vendor_id, MAX(invoice_total) AS invoice_total_max -- max gives the largest unpaid invoice
FROM invoices
WHERE invoice_total - credit_total - payment_total > 0 -- check if there is still an invoice total
GROUP BY vendor_id) AS total; -- condense each vendor_id to one group and apply each aggregate calculation to each group 
/* "AS total" needs to be included since this table is dervied from a SELECT statement */

-- exercise 6
SELECT vendor_name, vendor_city, vendor_state
FROM vendors
WHERE CONCAT(vendor_city, vendor_state) NOT IN -- concat vendor_city and vendor_state and check if it is in the subquery city_state 
	(SELECT DISTINCT CONCAT(vendor_city, vendor_state) AS city_state
    FROM vendors
    GROUP BY city_state -- group by concat of city and state only if the row count is > 1
    HAVING COUNT(*) > 1) -- if multiple city_state's exists, then group them into one row
ORDER BY vendor_state, vendor_city;

-- exercise 7 
SELECT vendor_name, invoice_number, invoice_date,
invoice_total
FROM invoices i JOIN vendors v -- join to get vendor_name
	ON i.vendor_id = v.vendor_id
WHERE invoice_date =
	(SELECT MAX(invoice_date) -- get max (oldest) invoice_date
	FROM invoices AS correlated_invoice 
    /* use the same invoices table in the inner query but with different alias to section
    where each vendor has the oldest invoice based on the outer query's vendor_id results */
	WHERE correlated_invoice.vendor_id = i.vendor_id) -- this will check all vendor's with the same id and get their oldest invoice_date
ORDER BY vendor_name;

/* Extra Example */
SELECT vendor_name, COUNT(*) AS total_invoice_foreach_vendor
FROM invoices i JOIN vendors v
ON i.vendor_id = v.vendor_id
GROUP BY vendor_name
ORDER BY vendor_name;


  
