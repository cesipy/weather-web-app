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

    public void cashUp(){
        List<Userx> userxList = userxRepository.findByRole(UserxRole.PREMIUM);
        for(Userx user : userxList){offSet(user);}
    }

    public void generateInvoice(Userx user){
        if(user.getInvoice() != null) {invoiceService.deleteInvoice(user.getInvoice());}
        Invoice invoice = new Invoice();
        invoice.setUserx(user);
        invoiceService.saveInvoice(invoice);
        user.setInvoice(invoice);
    }
    public void offSet(Userx user){
        if(user.getCreditCard().getBalance()-calculatePrice(user.getInvoice())>=0){
            invoiceService.setInvoiceOpen(user.getInvoice(),false);
            creditCardService.updateBalance(calculatePrice(user.getInvoice()), user.getCreditCard());
            writeEmail("Balance ist ok!", "Die Abrechnung war erfolgreich für User:" + user.getUsername());
        }else {
            userService.removeUserRole(user.getUsername(), UserxRole.PREMIUM);
            writeEmail("Balance ist nicht ok!", "Die Abrechnung war nicht erfolgreich für User" + user.getUsername());
        }
    }

    public  double calculatePrice(Invoice invoice){
        int localDateTime = LocalDateTime.now().getDayOfMonth();
        int invoiceDate = invoice.getCreateDate().getDayOfMonth();
        int premiumTime = localDateTime-invoiceDate+1;
        double pricePerDay = 1;
        return pricePerDay*premiumTime;
    }

    public void writeEmail(String subject, String body){
        String sendTo = "swaprojektarbeitwetterapp@gmail.com";
        emailService.sendEmail(sendTo, subject, body);
    }
}
