package at.qe.skeleton.internal.services;

import at.qe.skeleton.internal.model.AuditLog;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//3. service for AuditLog entities
@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void saveEntry(String message) {
        AuditLog al = new AuditLog();
        al.setMessage(message);
        al.setDate(LocalDateTime.now());
        auditLogRepository.save(al);
    }

    public void saveUserDeletedEntry(Userx userx) {
        saveEntry("User with username " + userx.getUsername() + " has been deleted!");
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}

