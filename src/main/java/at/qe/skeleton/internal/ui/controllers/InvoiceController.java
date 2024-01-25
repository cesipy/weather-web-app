package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for an Invoice.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class InvoiceController implements Serializable {

    @Autowired
    private transient InvoiceService invoiceService;

    /**
     * Attribute to cache the Invoice
     */
    private Invoice invoice;

    /**
     * Sets the current Invoice and reloads it form db. This Invoice is
     * targeted by any further calls of
     * {@link #doReloadInvoice()}, {@link #doSaveInvoice()} ()} and
     * {@link #doDeleteInvoice()}.
     *
     * @param invoice
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    /**
     * Returns the current Invoice.
     *
     * @return
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Action to save the current Invoice.
     */
    public void doSaveInvoice() {
        invoice = this.invoiceService.saveInvoice(invoice);
    }

    /**
     * Action to delete the current Invoice.
     */
    public void doDeleteInvoice() {
        this.invoiceService.deleteInvoice(invoice);
        invoice = null;
    }

}
