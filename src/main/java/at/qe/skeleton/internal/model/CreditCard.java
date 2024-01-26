package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing Credit Cards.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 *
 * @Entity Indicates that this class is a JPA entity.
 */
@Entity
public class CreditCard implements Serializable, Comparable<CreditCard> {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the credit card.
     *
     * @Id Marks this field as the primary key in the database.
     * @Column Specifies the details of the database column.
     */
    @Id
    @Column(length = 100)
    private String creditCard;

    /**
     * The balance associated with the credit card.
     */
    @Column
    private double balance;

    /**
     * The user associated with the credit card.
     *
     * @OneToOne Indicates a one-to-one relationship with another entity.
     * @JoinColumn Specifies the mapping of the foreign key column.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;

    /**
     * Sets the credit card number.
     *
     * @param creditCard The credit card number.
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Sets the balance associated with the credit card.
     *
     * @param balance The balance value.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the balance associated with the credit card.
     *
     * @return The balance value.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the user associated with the credit card.
     *
     * @param userx The user object.
     */
    public void setUserx(Userx userx) {
        this.userx = userx;
    }

    /**
     * Gets the user associated with the credit card.
     *
     * @return The user object.
     */
    public Userx getUserx() {
        return userx;
    }

    /**
     * Gets the credit card number.
     *
     * @return The credit card number.
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * Generates a hash code for the credit card.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 11;
        hash = 59 * hash + Objects.hashCode(this.creditCard);
        return hash;
    }

    /**
     * Compares this credit card with another object for equality.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CreditCard)) {
            return false;
        }
        final CreditCard other = (CreditCard) obj;
        return Objects.equals(this.creditCard, other.getCreditCard());
    }

    /**
     * Returns a string representation of the credit card.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "at.qe.skeleton.model.CreditCard[ id=" + creditCard + " ]";
    }

    /**
     * Compares this credit card with another for ordering.
     *
     * @param o The credit card to compare.
     * @return A negative integer, zero, or a positive integer as this credit card
     *         is less than, equal to, or greater than the specified credit card.
     */
    @Override
    public int compareTo(CreditCard o) {
        return this.creditCard.compareTo(o.getCreditCard());
    }
}
