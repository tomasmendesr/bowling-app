package jobsity.core.services.pinfall;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;

import java.util.List;

public interface PinfallService {
    /**
     * Returns the pinfall from a frame
     * @param frame
     * @return
     */
    List<Pinfall> findByFrame(final Frame frame);

    void save(final Pinfall pinfall);

    /**
     * Saves a new pinfall related with a frame.
     * @param frame
     * @return
     * @throws jobsity.core.exceptions.BowlingApplicationException in case the sum of the pinfalls adds up to more than 10
     */
    void saveNewPinfall(final Frame frame, final int pinfalls);

    void validateNewPinfallInFrame(final Frame frame, final Pinfall newPinfall);
}