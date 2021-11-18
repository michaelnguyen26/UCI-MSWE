/*question 12*/

-- this is one statement 
SELECT vendor_name, vendor_city -- hover over or click line to change insertion point
FROM vendors
WHERE vendor_id = 34;

-- this is a second statement 
SELECT COUNT(*) AS number_of_invoices,
    SUM(invoice_total - payment_total - credit_total) AS total_due
FROM invoices
WHERE vendor_id = 34;

/* the bottom tab in result grid allows us to see each result
from the SELECT keyword */