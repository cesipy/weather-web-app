package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing CreditCards.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Entity
public class CreditCard implements Serializable, Comparable<CreditCard> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String creditCard;
    @Column
    private  double balance;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;



    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setBalance(double balance) {this.balance = balance;}

    public double getBalance() {return balance;}


    public void setUserx(Userx userx) {
        this.userx = userx;
    }
    public Userx getUserx(){return userx;}

    public String getCreditCard() {
        return creditCard;
    }


    @Override
    public int hashCode() {
        int hash = 11;
        hash = 59 * hash + Objects.hashCode(this.creditCard);
        return hash;
    }

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


    @Override
    public String toString() {
        return "at.qe.skeleton.model.CreditCard[ id=" + creditCard + " ]";
    }

    @Override
    public int compareTo(CreditCard o) {
        return this.creditCard.compareTo(o.getCreditCard());
    }
}