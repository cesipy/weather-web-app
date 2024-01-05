package at.qe.skeleton.internal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

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

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;

    public String getCreditCard() {
        return creditCard;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
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