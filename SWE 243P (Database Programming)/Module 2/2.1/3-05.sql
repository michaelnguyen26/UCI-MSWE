SELECT invoice_total, payment_total, credit_total,
       invoice_total - payment_total - credit_total AS balance_due
FROM invoices;

/* Question 5 & 6 */
SELECT invoice_id, 
       invoice_id + 7 * 3 AS multiply_first, 
       (invoice_id + 7) * 3 AS add_first
FROM invoices -- when invoice_id is deleted add a ";" here
ORDER BY invoice_id; /* remove this line to see that this does not 
					sort by invoice_id */ 

SELECT invoice_id, 
       invoice_id / 3 AS decimal_quotient,
       invoice_id DIV 3 AS integer_quotient,
       invoice_id % 3 AS remainder
FROM invoices
ORDER BY invoice_id;