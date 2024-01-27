package at.qe.skeleton.internal.model;

/**
 * Represents an email request with information such as the recipient, subject, and body.
 * This class is used for sending email messages.
 */
public class EmailRequest {

    /**
     * The email address of the recipient.
     */
    private String to;

    /**
     * The subject of the email.
     */
    private String subject;

    /**
     * The body/content of the email.
     */
    private String body;

    /**
     * Constructs a new EmailRequest with the specified recipient, subject, and body.
     *
     * @param to      The email address of the recipient.
     * @param subject The subject of the email.
     * @param body    The body/content of the email.
     */
    public EmailRequest(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Gets the email address of the recipient.
     *
     * @return The email address of the recipient.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the email address of the recipient.
     *
     * @param to The email address of the recipient.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the subject of the email.
     *
     * @return The subject of the email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the body/content of the email.
     *
     * @return The body/content of the email.
     */
    public String getBody() {
        return body;
    }
}
