package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.Invoice;

public interface InvoiceRepository extends AbstractRepository<Invoice, String>{
    Invoice findFirstByInvoiceId(String invoiceId);
}
