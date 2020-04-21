package jobsity.core.repositories;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FrameRepository extends JpaRepository<Frame, Long> {

    /**
     * Returns a list of frames from a player.
     * @param player
     * @return
     */
    List<Frame> findByPlayer(final Player player);
}
