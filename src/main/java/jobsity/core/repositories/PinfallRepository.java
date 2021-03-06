package jobsity.core.repositories;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PinfallRepository extends JpaRepository<Pinfall, Long> {

    /**
     * Returns a list of pinfall from a frame.
     * @param frame
     * @return
     */
    List<Pinfall> findByFrame(final Frame frame);
}
