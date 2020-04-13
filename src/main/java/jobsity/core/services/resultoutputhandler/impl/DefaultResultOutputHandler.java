package jobsity.core.services.resultoutputhandler.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.entities.Player;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.services.game.GameService;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.services.player.PlayerService;
import jobsity.core.services.resultoutputhandler.ResultOutputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DefaultResultOutputHandler implements ResultOutputHandler {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PinfallService pinfallService;

    @Override
    public void printGameResult() {
        System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "FRAME", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        final Map<Player, List<Frame>> framesByPlayer = playerService.getFramesByPlayer();
        framesByPlayer.forEach((player, frames) -> {
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", player.getName(), "", "", "", "", "", "", "", "", "", "");
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Pinfalls", getFramePinfallsOutput(frames.get(0)), getFramePinfallsOutput(frames.get(1)), getFramePinfallsOutput(frames.get(2)), getFramePinfallsOutput(frames.get(3)), getFramePinfallsOutput(frames.get(4)), getFramePinfallsOutput(frames.get(5)), getFramePinfallsOutput(frames.get(6)), getFramePinfallsOutput(frames.get(7)), getFramePinfallsOutput(frames.get(8)), getLastFrameOutput(frames));
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Score", "", "", "", "", "", "", "", "", "", "", "", "");
        });
    }

    @Override
    public void finishGame() {
        if (!gameService.gameHasFinished()) {
            throw new BowlingApplicationException("There isn't enought data to finish the game. Please check the the file.");
        }
        gameService.setFinalScores();
    }

    private String getFramePinfallsOutput(final Frame frame) {
        final List<Pinfall> pinfalls = pinfallService.findByFrame(frame);
        if (pinfalls.size() == 1) return "    X";
        final int firstShoot = pinfalls.get(0).getQuantity();
        final int secondShoot = pinfalls.get(1).getQuantity();
        if (pinfalls.size() == 1) return "    X";
        if (firstShoot + secondShoot == 10) return " " + firstShoot + "  /";
        return " " + firstShoot + "  " + secondShoot;
    }

    private String getLastFrameOutput(final List<Frame> frames) {
        Frame frame9 = frames.get(9);
        final List<Pinfall> pinfallsFromFrame9 = pinfallService.findByFrame(frame9);
        if (pinfallsFromFrame9.size() == 1) {
            String result = " X";
            Frame frame10 = frames.get(10);
            final List<Pinfall> pinfallsFromFrame10 = pinfallService.findByFrame(frame10);
            if (pinfallsFromFrame10.get(0).getQuantity() == 10) {
                result = result + " X ";
                Frame frame11 = frames.get(11);
                final List<Pinfall> pinfallsFromFrame11 = pinfallService.findByFrame(frame11);
                if (pinfallsFromFrame11.get(0).getQuantity() == 10) {
                    return result + "X";
                }
                return result + pinfallsFromFrame11.get(0).getQuantity();
            }
            return result + " " + pinfallsFromFrame10.get(0).getQuantity() + " " + pinfallsFromFrame10.get(1).getQuantity();
        }
        return getFramePinfallsOutput(frame9);
    }
}
