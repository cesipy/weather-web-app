package at.qe.skeleton.external.controllers;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    /**
     * Displays a warning message about a location not being found.
     *
     */
    public void showWarnMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning:", message));
    }

    /**
     * Displays an informational message about a location not being found.
     *
     */
    public void showInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", message));
    }
}
