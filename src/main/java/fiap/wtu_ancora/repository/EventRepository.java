package fiap.wtu_ancora.repository;

import fiap.wtu_ancora.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT DISTINCT e FROM Event e " +
            "LEFT JOIN e.users u " +
            "LEFT JOIN e.units un " +
            "LEFT JOIN un.users unitUsers " +
            "WHERE u.email = :email OR unitUsers.email = :email")
    List<Event> findEventsByUserEmail(@Param("email") String email);

    Optional<Event> findByPublicLink(String publicLink);
}
