package fiap.wtu_ancora.repository;

import fiap.wtu_ancora.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String username);

    User findUserByEmail(String username);

    @Query("SELECT u FROM users u JOIN FETCH u.unit")
    List<User> findAllUsersWithUnits();

     List<User> findByUnitId (Long unitId);
}
