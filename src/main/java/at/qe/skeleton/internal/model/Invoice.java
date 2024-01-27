package at.qe.skeleton.internal.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an invoice in the system.
 *
 * This class is annotated with JPA annotations to enable database persistence.
 */
@Entity
public class Invoice implements Serializable, Comparable<Invoice> {

    /**
     * The unique identifier for the invoice.
     *
     * @Id Marks this field as the primary key in the database.
     * @Column Specifies the details of the database column.
     */
    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String invoiceId;

    /**
     * The user associated with the invoice.
     *
     * @ManyToOne Indicates a many-to-one relationship with another entity.
     * @JoinColumn Specifies the mapping of the foreign key column.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private Userx userx;

    /**
     * The date and time when the invoice was created.
     *
     * @CreationTimestamp Generates the timestamp when the entity is created.
     */
    @CreationTimestamp
    private LocalDateTime createDate;

    /**
     * Indicates whether the invoice is open or closed.
     */
    private boolean invoiceOpen;

    /**
     * Default constructor for creating an invoice.
     * Initializes the invoiceId, createDate, and sets invoiceOpen to true.
     */
    public Invoice() {
        this.invoiceId = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
        this.invoiceOpen = true;
    }

    /**
     * Checks if the invoice is open.
     *
     * @return True if the invoice is open, false otherwise.
     */
    public boolean isInvoiceOpen() {
        return invoiceOpen;
    }

    /**
     * Sets the status of the invoice (open or closed).
     *
     * @param invoiceOpen True if the invoice is open, false otherwise.
     */
    public void setInvoiceOpen(boolean invoiceOpen) {
        this.invoiceOpen = invoiceOpen;
    }

    /**
     * Gets the unique identifier for the invoice.
     *
     * @return The invoice identifier.
     */
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets the user associated with the invoice.
     *
     * @param userx The user object.
     */
    public void setUserx(Userx userx) {
        this.userx = userx;
    }

    /**
     * Gets the date and time when the invoice was created.
     *
     * @return The date and time of the invoice creation.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Generates a hash code for the invoice.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = 59 * hash + Objects.hashCode(this.invoiceId);
        return hash;
    }

    /**
     * Compares this invoice with another object for equality.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
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

    /**
     * Returns a string representation of the invoice.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "at.qe.skeleton.model.Invoice[ id=" + getInvoiceId() + " ]";
    }

    /**
     * Compares this invoice with another for ordering.
     *
     * @param o The invoice to compare.
     * @return A negative integer, zero, or a positive integer as this invoice
     *         is less than, equal to, or greater than the specified invoice.
     */
    @Override
    public int compareTo(Invoice o) {
        return this.invoiceId.compareTo(o.getInvoiceId());
    }
}
