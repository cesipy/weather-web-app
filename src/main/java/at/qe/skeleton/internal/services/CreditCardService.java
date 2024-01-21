package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.repositories.CreditCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCard saveCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
    @Transactional
    public void deleteCreditCard(String creditCard) {
        System.out.println("kreditkarte wird gelöscht mit der nummer" + creditCard);
        creditCardRepository.deleteByCreditCard(creditCard);;
    }
    public void updateBalance(double price, CreditCard creditCard){
        creditCard.setBalance(creditCard.getBalance()-price);
        saveCreditCard(creditCard);
    }

    public CreditCard getCreditCardById(String creditcard){
        return creditCardRepository.findFirstByCreditCard(creditcard);
    }
}
