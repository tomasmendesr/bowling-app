package jobsity.core.services.frame;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;

import java.util.List;

public interface FrameService {
    void handleFrame(final String playerName, final int pinfalls);
    Frame getLastFrame(final List<Frame> frames);
    Frame getCurrentFrame(final Frame lastFrame);
    Frame saveFrame(final Player player, final int frameNumber);
    Boolean isFrameComplete(final Frame frame);
    List<Frame> findByPlayer(Player player);
}
