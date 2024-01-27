package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.model.UserxRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing {@link Userx} entities.
 *
 * This interface extends the abstract interface AbstractRepository
 * and specifies the type of entity (Userx) and the data type of
 * the primary key (String).
 *
 * @param </Userx> The type of entity managed by this repository (Userx).
 * @param </String>  The data type of the primary key (String).
 *
 * @see AbstractRepository
 * @see Userx
 */
public interface UserxRepository extends AbstractRepository<Userx, String> {

    /**
     * Finds the first user entity with the specified username.
     *
     * @param username The username to search for.
     * @return The first user entity with the specified username, or null if not found.
     */
    Userx findFirstByUsername(String username);

    /**
     * Finds users whose username contains the specified substring.
     *
     * @param username The substring to search for in usernames.
     * @return A list of users whose username contains the specified substring.
     */
    List<Userx> findByUsernameContaining(String username);

    /**
     * Finds the first user entity whose username contains the specified substring.
     *
     * @param username The substring to search for in usernames.
     * @return The first user entity whose username contains the specified substring, or null if not found.
     */
    Userx findFirstByUsernameContaining(String username);

    /**
     * Finds users whose full name (concatenation of first name and last name) matches the specified value.
     *
     * @param wholeName The full name to search for.
     * @return A list of users whose full name matches the specified value.
     */
    @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

    /**
     * Finds users who have the specified role.
     *
     * @param role The role to search for.
     * @return A list of users who have the specified role.
     */
    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    List<Userx> findByRole(@Param("role") UserxRole role);
}
