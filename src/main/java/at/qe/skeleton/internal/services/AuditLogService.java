package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.AuditLog;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing audit log entries.
 * This service is responsible for saving audit log entries, specifically
 * user deletion entries, and retrieving all audit log entries.
 *
 * @see Service
 */
@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Saves a generic audit log entry with the specified message and current timestamp.
     *
     * @param message The message to be saved in the audit log.
     */
    public void saveEntry(String message) {
        AuditLog al = new AuditLog();
        al.setMessage(message);
        al.setDate(LocalDateTime.now());
        auditLogRepository.save(al);
    }

    /**
     * Saves an audit log entry for a user deletion with the specified user.
     *
     * @param userx The user whose deletion is being logged.
     */
    public void saveUserDeletedEntry(Userx userx) {
        saveEntry("User with username " + userx.getUsername() + " has been deleted!");
    }

    /**
     * Retrieves all audit log entries from the repository.
     *
     * @return A list of all audit log entries.
     */
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}
