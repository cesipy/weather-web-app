package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.AuditLog;

/**
 * Repository interface for database access to AuditLog entities.
 * This interface extends the abstract interface AbstractRepository
 * and specifies the type of entity (AuditLog) and the data type of
 * the primary key (Long).
 *
 * @param </AuditLog> The type of entity managed by this repository (AuditLog).
 * @param </Long>     The data type of the primary key (Long).
 *
 * @see AbstractRepository
 * @see AuditLog
 */
public interface AuditLogRepository extends AbstractRepository<AuditLog, Long> {
    // Methods in this interface can use the ones defined in the AbstractRepository interface.
}

