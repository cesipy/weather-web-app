package at.qe.skeleton.internal.model;

import at.qe.skeleton.external.model.WeatherDataField;
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
*/
@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the user.
     *
     * @Id Marks this field as the primary key in the database.
     * @Column Specifies the details of the database column.
     * @NotEmpty Ensures that the username is not empty.
     */

    @Id
    @Column(length = 100)
    @NotEmpty(message = "Name can not be empty")
    private String username;

    /**
     * The user who created this user.
     *
     * @ManyToOne Indicates a many-to-one relationship with another entity.
     */

    @ManyToOne(optional = true) //Changed to true
    private Userx createUser;

    /**
     * The date and time when the user was created.
     *
     * @Column Specifies the details of the database column.
     * @CreationTimestamp Generates the timestamp when the entity is created.
     */
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    /**
     * The user who last updated this user.
     *
     * @ManyToOne Indicates a many-to-one relationship with another entity.
     */
    @ManyToOne(optional = true)
    private Userx updateUser;

    /**
     * The date and time when the user was last updated.
     *
     * @UpdateTimestamp Generates the timestamp when the entity is updated.
     */
    @UpdateTimestamp
    private LocalDateTime updateDate;

    /**
     * The password associated with the user.
     *
     * @NotEmpty Ensures that the password is not empty.
     * @Size Specifies the size constraints for the password.
     */

    @NotEmpty
    @Size(min = 5, max = 64)
    private String password;

    /**
     * The first name of the user.
     *
     * @NotEmpty Ensures that the first name is not empty.
     */

    @NotEmpty
    private String firstName;

    /**
     * The last name of the user.
     *
     * @NotEmpty Ensures that the last name is not empty.
     */

    @NotEmpty
    private String lastName;

    /**
     * The email address of the user.
     *
     * @NotEmpty Ensures that the email address is not empty.
     */

    @NotEmpty
    private String email;

    /**
     * The phone number of the user.
     */
    private String phone;

    /**
     * The date and time when the user became a premium user.
     */
    private LocalDateTime premiumSince;

    /**
     * The credit card associated with the user.
     *
     * @OneToOne Indicates a one-to-one relationship with another entity.
     * @mappedBy Specifies the mapping of the field in the related entity.
     * @cascade Specifies the cascade behavior for the relationship.
     */
    @OneToOne(mappedBy = "userx", cascade = CascadeType.ALL, orphanRemoval = true)
    private CreditCard creditCard;

    /**
     * The invoice associated with the user.
     *
     * @OneToOne Indicates a one-to-one relationship with another entity.
     * @mappedBy Specifies the mapping of the field in the related entity.
     * @cascade Specifies the cascade behavior for the relationship.
     */
    @OneToOne(mappedBy = "userx", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invoice invoice;

    /**
     * The selected weather data fields associated with the user.
     *
     * @ElementCollection Specifies a collection of basic types or embeddable objects.
     * @CollectionTable Specifies the mapping for the collection table.
     * @Column Specifies the mapping for the collection element.
     * @Enumerated Specifies the type used for the elements of the collection.
     */
    @ElementCollection
    @CollectionTable(name = "USERX_SELECTEDFIELDS", joinColumns = @JoinColumn(name = "USERX_USERNAME"))
    @Column(name = "SELECTEDFIELD")
    @Enumerated(EnumType.STRING)
    private List<WeatherDataField> selectedFields;

    /**
     * Gets the invoice associated with the user.
     *
     * @return The invoice associated with the user.
     */
    public Invoice getInvoice() {return this.invoice;}

    /**
     * Sets the invoice associated with the user.
     *
     * @param invoices The invoice associated with the user.
     */
    public void setInvoice(Invoice invoices) {this.invoice = invoices;}

    /**
     * Gets the credit card associated with the user.
     *
     * @return The credit card associated with the user.
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * The status of the user.
     */
    boolean enabled;

    /**
     * Set of roles associated with the user.
     */
    @ElementCollection(targetClass = UserxRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserxRole")
    @Enumerated(EnumType.STRING)
    private Set<UserxRole> roles;

    /**
     * Gets the premium start date for the user.
     *
     * @return The date and time when the user became a premium user.
     */
    public LocalDateTime getPremiumSince() {
        return premiumSince;
    }

    /**
     * Sets the premium start date for the user.
     *
     * @param premiumSince The date and time when the user became a premium user.
     */
    public void setPremiumSince(LocalDateTime premiumSince) {
        this.premiumSince = premiumSince;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password associated with the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password associated with the user.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone The phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return True if the user is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the status of the user (enabled or disabled).
     *
     * @param enabled True if the user is enabled, false otherwise.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the roles associated with the user.
     *
     * @return The set of roles.
     */
    public Set<UserxRole> getRoles() {
        return roles;
    }

    /**
     * Sets the roles associated with the user.
     *
     * @param roles The set of roles.
     */

    public void setRoles(Set<UserxRole> roles) {
        this.roles = roles;
    }

    /**
     * Gets the user who created this user.
     *
     * @return The user who created this user.
     */
    public Userx getCreateUser() {
        return createUser;
    }

    /**
     * Sets the user who created this user.
     *
     * @param createUser The user who created this user.
     */
    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    /**
     * Gets the date and time when the user was created.
     *
     * @return The date and time when the user was created.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date and time when the user was created.
     *
     * @param createDate The date and time when the user was created.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the user who last updated this user.
     *
     * @return The user who last updated this user.
     */
    public Userx getUpdateUser() {
        return updateUser;
    }

    /**
     * Sets the user who last updated this user.
     *
     * @param updateUser The user who last updated this user.
     */
    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Gets the date and time when the user was last updated.
     *
     * @return The date and time when the user was last updated.
     */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the date and time when the user was last updated.
     *
     * @param updateDate The date and time when the user was last updated.
     */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Sets the credit card associated with the user.
     *
     * @param creditCard The credit card associated with the user.
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Generates a hash code for the user.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    /**
     * Compares this user with another object for equality.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Userx)) {
            return false;
        }
        final Userx other = (Userx) obj;
        return Objects.equals(this.username, other.username);
    }

    /**
     * Returns a string representation of the user.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return The unique identifier.
     */
    @Override
    public String getId() {
        return getUsername();
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id The unique identifier.
     */
    public void setId(String id) {
        setUsername(id);
    }

    /**
     * Checks if the user is new (not yet persisted).
     *
     * @return True if the user is new, false otherwise.
     */
    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    /**
     * Compares this user to another user based on their usernames.
     *
     * @param o The Userx object to compare to.
     * @return a negative integer, zero, or a positive integer as this user
     *         is less than, equal to, or greater than the specified user.
     */
    @Override
    public int compareTo(Userx o) {
        return this.username.compareTo(o.getUsername());
    }

    /**
     * Gets the list of selected weather data fields for this user.
     *
     * @return List of WeatherDataField objects representing the selected fields.
     */
    public List<WeatherDataField> getSelectedFields() {
        return selectedFields;
    }

    /**
     * Sets the list of selected weather data fields for this user.
     *
     * @param selectedFields List of WeatherDataField objects to set as selected.
     */
    public void setSelectedFields(List<WeatherDataField> selectedFields) {
        this.selectedFields = selectedFields;
    }
}
