package jobsity.core.services.frame;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;

import java.util.List;

public interface FrameService {

    /**
     * Save the pinfalls for a player on a frame
     * @param playerName
     * @param pinfalls
     */
    void handleFrame(final String playerName, final int pinfalls);

    /**
     * Returns the frame with max frameNumber
     */
    Frame getLastFrame(final List<Frame> frames);

    /**
     * If the lastFrame received isn't complete this returns it. Otherwise create a new one and returns it.
     * @param lastFrame
     * @return
     */
    Frame getCurrentFrame(final Frame lastFrame);

    /***
     * Save the frame in the db.
     * @param player
     * @param frameNumber
     * @return
     */
    Frame saveFrame(final Player player, final int frameNumber);

    /**
     * Returns true if the frame is complete.
     * @param frame
     * @return
     */
    Boolean isFrameComplete(final Frame frame);

    /**
     * Returns all the frames for a specific player
     * @param player
     * @return
     */
    List<Frame> findByPlayer(Player player);
}
