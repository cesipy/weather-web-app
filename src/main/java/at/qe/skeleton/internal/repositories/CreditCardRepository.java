package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.CreditCard;

public interface CreditCardRepository extends AbstractRepository<CreditCard, String> {
    CreditCard findFirstByCreditCard(String creditCard);


}
