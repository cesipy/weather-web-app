package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.UserxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for managing CreditCard entities.
 * This controller is responsible for handling CreditCard-related operations, such as saving and deleting CreditCard entities.
 *
 * @see Component
 * @see Scope
 */
@Component
@Scope("view")
public class CreditCardController implements Serializable {

    @Autowired
    private transient UserxService userxService;

    @Autowired
    private transient CreditCardService creditCardService;

    /**
     * Attribute to cache the CreditCard.
     */
    private CreditCard creditCard = new CreditCard();

    /**
     * Sets the current CreditCard and reloads it from the database. This CreditCard is
     * targeted by any further calls of
     * {@link #doReloadCreditCard()}, {@link #doSaveCreditCard()} ()}, and
     * {@link #doDeleteUser()}.
     *
     * @param creditCard The CreditCard entity to set as the current CreditCard.
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Returns the current CreditCard.
     *
     * @return The current CreditCard.
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Action to save the current CreditCard.
     * This method sets the authenticated user as the owner of the CreditCard and saves it to the database.
     */
    public void doSaveCreditCard() {
        creditCard.setUserx(userxService.getAuthenticatedUser());
        creditCard = this.creditCardService.saveCreditCard(creditCard);
    }

    /**
     * Action to delete the current CreditCard.
     * This method deletes the current CreditCard from the database.
     */
    public void doDeleteCreditCard() {
        this.creditCardService.deleteCreditCard(creditCard.getCreditCard());
        creditCard = null;
    }

}
