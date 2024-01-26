package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.repositories.CreditCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service class for managing CreditCard entities.
 * This service provides methods for saving, deleting, updating balances, and retrieving CreditCard entities.
 *
 * @see Component
 * @see Scope
 */
@Component
@Scope("application")
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    /**
     * Saves a CreditCard entity.
     *
     * @param creditCard The CreditCard entity to be saved.
     * @return The saved CreditCard entity.
     */
    public CreditCard saveCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    /**
     * Deletes a CreditCard entity with the specified credit card number.
     *
     * @param creditCard The credit card number of the CreditCard entity to be deleted.
     */
    @Transactional
    public void deleteCreditCard(String creditCard) {
        creditCardRepository.deleteByCreditCard(creditCard);
    }

    /**
     * Updates the balance of a CreditCard entity after a transaction.
     *
     * @param price      The transaction amount.
     * @param creditCard The CreditCard entity to be updated.
     */
    public void updateBalance(double price, CreditCard creditCard) {
        creditCard.setBalance(creditCard.getBalance() - price);
        saveCreditCard(creditCard);
    }

    /**
     * Retrieves a CreditCard entity by its credit card number.
     *
     * @param creditcard The credit card number of the CreditCard entity to be retrieved.
     * @return The CreditCard entity with the specified credit card number.
     */
    public CreditCard getCreditCardById(String creditcard) {
        return creditCardRepository.findFirstByCreditCard(creditcard);
    }
}
