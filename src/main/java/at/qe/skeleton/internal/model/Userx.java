package at.qe.skeleton.internal.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


//constructor?

/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
*/
@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String username;

    @ManyToOne(optional = false)
    private Userx createUser;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;
    @ManyToOne(optional = true)
    private Userx updateUser;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToOne(mappedBy = "userx", cascade = CascadeType.ALL, orphanRemoval = true)
    private CreditCard creditCard;

    @OneToMany(mappedBy = "userx", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }


    public CreditCard getCreditCard() {
        return creditCard;
    }

    boolean enabled;

    @ElementCollection(targetClass = UserxRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserxRole")
    @Enumerated(EnumType.STRING)
    private Set<UserxRole> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserxRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserxRole> roles) {
        this.roles = roles;
    }

    public Userx getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Userx getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

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

    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int compareTo(Userx o) {
        return this.username.compareTo(o.getUsername());
    }

}
