package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }
    public void setInvoiceOpen(Invoice invoice, boolean isOpen){
        invoice.setInvoiceOpen(isOpen);
        saveInvoice(invoice);
    }
}