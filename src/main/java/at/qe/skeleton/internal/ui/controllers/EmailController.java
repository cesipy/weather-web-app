package at.qe.skeleton.internal.ui.controllers;

import at.qe.skeleton.internal.services.EmailService;
import at.qe.skeleton.internal.model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling email-related operations.
 * This controller is responsible for processing HTTP requests related to sending emails.
 *
 * @see Controller
 */
@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Handles HTTP POST requests to send an email.
     *
     * @param emailRequest The EmailRequest object containing email details (to, subject, body).
     * @return A message indicating the status of the email sending process.
     */
    @PostMapping("/send-email")
    @ResponseBody
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return "Email sent successfully!";
    }
}
