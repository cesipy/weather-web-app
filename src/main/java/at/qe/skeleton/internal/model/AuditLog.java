package at.qe.skeleton.internal.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an audit log entry in the system.
 * This class is annotated with JPA annotations to enable database persistence.
 *
 * @Entity Indicates that this class is a JPA entity.
 */
@Entity
public class AuditLog implements Persistable<Long>, Serializable {

    /**
     * The unique identifier for the audit log entry.
     *
     * @Id Marks this field as the primary key in the database.
     * @GeneratedValue Specifies the strategy for generating the ID value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The message associated with the audit log entry.
     *
     * @return The audit log message.
     */
    private String message;

    /**
     * The date and time when the audit log entry was created.
     *
     * @Temporal Specifies the type of temporal data.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    /**
     * Gets the message associated with the audit log entry.
     *
     * @return The audit log message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for the audit log entry.
     *
     * @param message The audit log message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the date and time when the audit log entry was created.
     *
     * @return The date and time of the audit log entry.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date and time for the audit log entry.
     *
     * @param date The date and time of the audit log entry.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Sets the unique identifier for the audit log entry.
     *
     * @param id The unique identifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier for the audit log entry.
     *
     * @return The unique identifier.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Checks if the audit log entry is new (not yet persisted in the database).
     *
     * @return True if the entry is new, false otherwise.
     */
    @Override
    public boolean isNew() {
        return id == null;
    }

    /**
     * Compares this audit log entry with another object for equality.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id);
    }

    /**
     * Generates a hash code for the audit log entry.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
