package at.qe.skeleton.internal.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Invoice implements Serializable, Comparable<Invoice> {
    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String invoiceId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;

    @CreationTimestamp
    private LocalDateTime createDate;

    private boolean invoiceOpen;

    public Invoice() {
        this.invoiceId = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.invoiceOpen = true;
    }

    public boolean isInvoiceOpen() {return invoiceOpen;}

    public void setInvoiceOpen(boolean invoiceOpen) {
        this.invoiceOpen = invoiceOpen;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setUserx(Userx userx) {this.userx = userx;}

    public LocalDateTime getCreateDate() {return createDate;}


    @Override
    public int hashCode() {
        int hash = 13;
        hash = 59 * hash + Objects.hashCode(this.invoiceId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Invoice)) {
            return false;
        }
        final Invoice other = (Invoice) obj;
        return Objects.equals(this.invoiceId, other.getInvoiceId());
    }


    @Override
    public String toString() {
        return "at.qe.skeleton.model.Invoice[ id=" + getInvoiceId() + " ]";
    }

    @Override
    public int compareTo(Invoice o) {
        return this.invoiceId.compareTo(o.getInvoiceId());
    }
}
