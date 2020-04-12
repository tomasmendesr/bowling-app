package jobcity.core.services.resultoutputhandler.impl;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;
import jobcity.core.entities.Player;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.services.game.GameService;
import jobcity.core.services.pinfall.PinfallService;
import jobcity.core.services.player.PlayerService;
import jobcity.core.services.resultoutputhandler.ResultOutputHandler;
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
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Pinfalls", getFramePinfallsOutput(frames.get(0)), getFramePinfallsOutput(frames.get(1)), getFramePinfallsOutput(frames.get(2)), getFramePinfallsOutput(frames.get(3)), getFramePinfallsOutput(frames.get(4)), getFramePinfallsOutput(frames.get(5)), getFramePinfallsOutput(frames.get(6)), getFramePinfallsOutput(frames.get(7)), getFramePinfallsOutput(frames.get(8)), getFramePinfallsOutput(frames.get(9)));
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
}
