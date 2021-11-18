/*question 9 (use Ctrl+T top open a new tab) */
SELECT COUNT(*) AS number_of_invoices, 
SUM(invoice_total) AS grand_invoice_total 
FROM invoices