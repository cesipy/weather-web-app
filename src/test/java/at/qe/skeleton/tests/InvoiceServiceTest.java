package at.qe.skeleton.tests;
import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.repositories.InvoiceRepository;
import at.qe.skeleton.internal.services.InvoiceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class InvoiceServiceTest {

    @Autowired
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @Test
    void saveInvoice() {
        Invoice invoice = new Invoice();

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);


        assertNotNull(savedInvoice);
        assertEquals(invoice, savedInvoice);
    }

    @Test
    void deleteInvoice() {
        Invoice invoice = new Invoice();

        invoiceService.deleteInvoice(invoice);

        Mockito.verify(invoiceRepository, Mockito.times(1)).delete(invoice);
    }

    @Test
    void updateInvoiceStatus() {
        Invoice invoice = new Invoice();

        invoiceService.setInvoiceOpen(invoice, false);

        assertFalse(invoice.isInvoiceOpen());
        Mockito.verify(invoiceRepository, Mockito.times(1)).save(invoice);
    }
}
