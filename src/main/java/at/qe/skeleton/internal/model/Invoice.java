package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Invoice implements Serializable, Comparable<Invoice> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String invoiceId;

    private LocalDateTime invoiceDate;

    private boolean invoiceOpen;

    public Invoice() {
        this.invoiceId = UUID.randomUUID().toString();
        this.invoiceDate = LocalDateTime.now();
        this.invoiceOpen = true;
    }

    public boolean isInvoiceOpen() {
        return invoiceOpen;
    }

    public void setInvoiceOpen(boolean invoiceOpen) {
        this.invoiceOpen = invoiceOpen;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;

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
