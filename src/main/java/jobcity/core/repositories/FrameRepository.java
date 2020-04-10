package jobcity.core.repositories;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Player;
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

    /**
     * Returns a frame by a frameNumber
     * @param frameNumber
     * @return
     */
    Frame findByFrameNumber(final int frameNumber);
}
