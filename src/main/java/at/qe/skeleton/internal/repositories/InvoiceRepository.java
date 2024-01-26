package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.Invoice;

/**
 * Repository interface for database access to Invoice entities.
 * This interface extends the abstract interface AbstractRepository
 * and specifies the type of entity (Invoice) and the data type of
 * the primary key (String).
 *
 * @param <Invoice> The type of entity managed by this repository (Invoice).
 * @param <String>  The data type of the primary key (String).
 *
 * @see AbstractRepository
 * @see Invoice
 */
public interface InvoiceRepository extends AbstractRepository<Invoice, String> {

    /**
     * Finds the first invoice entity with the specified invoice ID.
     *
     * @param invoiceId The invoice ID to search for.
     * @return The first invoice entity with the specified invoice ID, or null if not found.
     */
    Invoice findFirstByInvoiceId(String invoiceId);
}
