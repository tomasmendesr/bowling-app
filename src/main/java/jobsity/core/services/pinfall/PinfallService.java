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

    /**
     * Saves a pinfall in the db
     * @param pinfall
     */
    void save(final Pinfall pinfall);

    /**
     * Saves a new pinfall related with a frame.
     * @param frame
     * @return
     * @throws jobsity.core.exceptions.BowlingApplicationException in case the sum of the pinfalls adds up to more than 10
     */
    void saveNewPinfall(final Frame frame, final int pinfalls);

    /**
     * Valiates if the new pinfall number is ok for a frame
     * @param frame
     * @param newPinfall
     */
    void validateNewPinfallInFrame(final Frame frame, final Pinfall newPinfall);

    /**
     * Returns the pinfall quantity of the first and second shoots
     * @param frame
     * @return
     */
    int calculateQuantityFromFrame(Frame frame);

    /**
     * Returns the pinfall quantity
     * @param frame
     * @param calculateOnlyFirstAndSecondShoot
     * @return
     */
    int calculateQuantityFromFrame(final Frame frame, boolean calculateOnlyFirstAndSecondShoot);

    /**
     * Returns true if the player has made a stricke for the current frame
     * @param frame
     * @return
     */
    boolean isAStrike(Frame frame);

    /**
     * Returns true if the quantity is 10
     * @param quantity
     * @return
     */
    boolean isAStrikeOrSpear(int quantity);

    /**
     * Returns the quanity of pinfalls for the first shoot
     * @param frame
     * @return
     */
    int getFirstPinfallQuantityFromFrame(Frame frame);
}
