package at.qe.skeleton.internal.ui.beans;

import at.qe.skeleton.internal.model.Invoice;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import at.qe.skeleton.internal.repositories.UserxRepository;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.EmailService;
import at.qe.skeleton.internal.services.InvoiceService;
import at.qe.skeleton.internal.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

/**
 * Managed Bean for performing cash-up operations.
 * This bean is responsible for cash-up operations, such as generating invoices,
 * calculating prices, offsetting balances, and sending emails.
 *
 * @see Component
 */
@Component
public class CashUpBean {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserxService userService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserxRepository userxRepository;

    /**
     * Performs cash-up operation for all users with the PREMIUM role.
     * This method retrieves a list of users with the PREMIUM role, and performs
     * the offset operation for each user.
     */
    public void cashUp(){
        List<Userx> userxList = userxRepository.findByRole(UserxRole.PREMIUM);
        for(Userx user : userxList){offSet(user);}
    }

    /**
     * Generates an invoice for the specified user.
     * If the user already has an invoice, it is deleted before generating a new one.
     *
     * @param user The user for whom to generate the invoice.
     */
    public void generateInvoice(Userx user){
        if(user.getInvoice() != null) {invoiceService.deleteInvoice(user.getInvoice());}
        Invoice invoice = new Invoice();
        invoice.setUserx(user);
        invoiceService.saveInvoice(invoice);
        user.setInvoice(invoice);
    }

    /**
     * Performs the offset operation for the specified user.
     * Checks if the user's credit card balance is sufficient to cover the invoice amount.
     * If yes, updates the credit card balance, marks the invoice as closed, and sends a success email.
     * If no, removes the PREMIUM role from the user, and sends a failure email.
     *
     * @param user The user for whom to perform the offset operation.
     */
    public void offSet(Userx user){
        if(user.getCreditCard().getBalance()-calculatePrice(user.getInvoice())>=0){
            invoiceService.setInvoiceOpen(user.getInvoice(),false);
            creditCardService.updateBalance(calculatePrice(user.getInvoice()), user.getCreditCard());
            writeEmail("Balance is OK!", "The settlement was successful for User: " + user.getUsername());
        } else {
            userService.removeUserRole(user.getUsername(), UserxRole.PREMIUM);
            writeEmail("Balance is not OK!", "The settlement was unsuccessful for User: " + user.getUsername());
        }
    }

    /**
     * Calculates the price based on the premium time.
     * Premium time is calculated as the difference in days between the current date
     * and the invoice creation date, plus one. The price per day is assumed to be 1.
     *
     * @param invoice The invoice for which to calculate the price.
     * @return The calculated price.
     */
    public  double calculatePrice(Invoice invoice){
        int localDateTime = LocalDateTime.now().getDayOfMonth();
        int invoiceDate = invoice.getCreateDate().getDayOfMonth();
        int premiumTime = localDateTime - invoiceDate + 1;
        double pricePerDay = 1;
        return pricePerDay * premiumTime;
    }

    /**
     * Sends an email with the specified subject and body to a predefined email address.
     *
     * @param subject The subject of the email.
     * @param body    The body of the email.
     */
    public void writeEmail(String subject, String body){
        String sendTo = "swaprojektarbeitwetterapp@gmail.com";
        emailService.sendEmail(sendTo, subject, body);
    }
}
