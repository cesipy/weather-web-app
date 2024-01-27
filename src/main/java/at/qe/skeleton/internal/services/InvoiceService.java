package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service class for managing Invoice entities.
 * This service provides methods for saving, deleting, and updating the status of Invoice entities.
 *
 * @see Component
 * @see Scope
 */
@Component
@Scope("application")
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * Saves an Invoice entity.
     *
     * @param invoice The Invoice entity to be saved.
     * @return The saved Invoice entity.
     */
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    /**
     * Deletes an Invoice entity.
     *
     * @param invoice The Invoice entity to be deleted.
     */
    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    /**
     * Updates the status of an Invoice entity.
     *
     * @param invoice The Invoice entity to be updated.
     * @param isOpen  The new status of the invoice.
     */
    public void setInvoiceOpen(Invoice invoice, boolean isOpen) {
        invoice.setInvoiceOpen(isOpen);
        saveInvoice(invoice);
    }
}
