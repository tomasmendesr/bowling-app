package jobcity.core.services.pinfall;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;

import java.util.List;

public interface PinfallService {
    /**
     * Returns the pinfall from a frame
     * @param frame
     * @return
     */
    List<Pinfall> findByFrame(final Frame frame);

    /**
     * Saves a new pinfall related with a frame.
     * @param frame
     * @return
     * @throws jobcity.core.exceptions.BowlingApplicationException in case the sum of the pinfalls adds up to more than 10
     */
    void save(final Frame frame, final int pinfalls);
}
