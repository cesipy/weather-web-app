package at.qe.skeleton.internal.model;

/**
 * Enumeration of available user roles.
 *
 * This class represents the different roles that a user can have in the system.
 * The roles include ADMIN, MANAGER, EMPLOYEE, and PREMIUM.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
public enum UserxRole {

    /**
     * Represents the role of an administrator with full access rights.
     */
    ADMIN,

    /**
     * Represents the role of a manager with elevated access rights.
     */
    MANAGER,

    /**
     * Represents the role of an employee with standard access rights.
     */
    EMPLOYEE,

    /**
     * Represents the role of a premium user with additional privileges.
     */
    PREMIUM
}
