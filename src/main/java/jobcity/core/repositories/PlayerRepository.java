package jobcity.core.repositories;

import jobcity.core.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByName(final String name);
}
