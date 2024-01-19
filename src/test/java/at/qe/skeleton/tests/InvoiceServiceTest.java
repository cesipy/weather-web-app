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
        // Arrange
        Invoice invoice = new Invoice();

        // Act
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        // Assert
        assertNotNull(savedInvoice);
        assertEquals(invoice, savedInvoice);
    }

    @Test
    void deleteInvoice() {
        // Arrange
        Invoice invoice = new Invoice();

        // Act
        invoiceService.deleteInvoice(invoice);

        // Assert
        Mockito.verify(invoiceRepository, Mockito.times(1)).delete(invoice);
    }

    @Test
    void updateInvoiceStatus() {
        // Arrange
        Invoice invoice = new Invoice();

        // Act
        invoiceService.setInvoiceOpen(invoice, false);

        // Assert
        assertFalse(invoice.isInvoiceOpen());
        Mockito.verify(invoiceRepository, Mockito.times(1)).save(invoice);
    }
}
