package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.model.CreditCard;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.UserxService;
import io.micrometer.common.util.StringUtils;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for a CreditCard.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("view")
public class CreditCardController implements Serializable {

    @Autowired
    private UserxService userxService;

    @Autowired
    private CreditCardService creditCardService;

    /**
     * Attribute to cache the CreditCard
     */
    private CreditCard creditCard =  new CreditCard();;

    /**
     * Sets the current CreditCard and reloads it form db. This CreditCard is
     * targeted by any further calls of
     * {@link #doReloadCreditCard()}, {@link #doSaveCreditCard()} ()} and
     * {@link #doDeleteUser()}.
     *
     * @param creditCard
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Returns the current CreditCard.
     *
     * @return
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Action to save the current CreditCard.
     */
    public void doSaveCreditCard() {
        creditCard.setUserx(userxService.getAuthenticatedUser());
        creditCard = this.creditCardService.saveCreditCard(creditCard);
    }

    /**
     * Action to delete the current CreditCard.
     */
    public void doDeleteCreditCard() {
        this.creditCardService.deleteCreditCard(creditCard.getCreditCard());
        creditCard = null;
    }

}
