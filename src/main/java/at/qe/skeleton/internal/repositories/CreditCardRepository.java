package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.CreditCard;

/**
 * Repository interface for database access to CreditCard entities.
 * This interface extends the abstract interface AbstractRepository
 * and specifies the type of entity (CreditCard) and the data type of
 * the primary key (String).
 *
 * @param <CreditCard> The type of entity managed by this repository (CreditCard).
 * @param <String>     The data type of the primary key (String).
 *
 * @see AbstractRepository
 * @see CreditCard
 */
public interface CreditCardRepository extends AbstractRepository<CreditCard, String> {

    /**
     * Finds the first credit card entity with the specified credit card number.
     *
     * @param creditCard The credit card number to search for.
     * @return The first credit card entity with the specified credit card number, or null if not found.
     */
    CreditCard findFirstByCreditCard(String creditCard);

    /**
     * Deletes the credit card entity with the specified credit card number.
     *
     * @param creditCard The credit card number of the entity to be deleted.
     */
    void deleteByCreditCard(String creditCard);
}

