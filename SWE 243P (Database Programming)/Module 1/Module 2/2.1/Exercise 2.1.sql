/* Section 1 Chapter 3: Exercises 8 - 14 */

-- exercise 8
SELECT vendor_name, vendor_contact_last_name, vendor_contact_first_name
FROM vendors
ORDER BY vendor_contact_last_name, vendor_contact_first_name; -- ASC is default

-- exercise 9 
SELECT CONCAT(vendor_contact_last_name, ", ", vendor_contact_first_name) AS full_name
FROM vendors

-- get the first char in last_name and test if the char is in the list 
WHERE LEFT(vendor_contact_last_name, 1) IN ('A', 'B', 'C', 'E')
ORDER BY vendor_contact_last_name, vendor_contact_first_name;

-- exercise 10 
SELECT invoice_due_date AS "Due Date",
invoice_total AS "Invoice Total", (0.10 * invoice_total) AS "10%",
(invoice_total + 0.10) AS "Plus 10%"
FROM invoices
WHERE invoice_total BETWEEN 500 AND 1000 -- test inclusivity range with BETWEEN keyword
ORDER BY invoice_due_date DESC;

-- exercise 11
SELECT invoice_number, invoice_total, 
(payment_total + credit_total) AS "payment_credit_total",
invoice_total - (payment_total - credit_total) AS "balance_due"
FROM invoices
WHERE invoice_total - (payment_total - credit_total) > 50 -- this is the balance_due column
ORDER BY invoice_total - (payment_total - credit_total) DESC -- sort balance_due column
LIMIT 5; -- limit to the top rows 1-5 which are the largest because of the ORDER BY

-- exercise 12
SELECT invoice_number, invoice_date, 
invoice_total - (payment_total - credit_total) AS "balance_due",
payment_date
FROM invoices
WHERE payment_date IS NULL; -- use keyword "IS NULL" only to check for null values

-- exercise 13
SELECT CURRENT_DATE, DATE_FORMAT(CURRENT_DATE, '%m-%d-%Y') -- use a capital "Y" to display four digit year format
AS "current_date";

-- exercise 14
SELECT "$50,000" AS starting_principal, -- select "data" as "column name"
0.065 * 50000 AS interest, 
50000 + (0.065 * 50000) AS principal_plus_interest;


