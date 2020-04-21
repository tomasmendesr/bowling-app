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

    private GameService gameService;
    private PlayerService playerService;
    private PinfallService pinfallService;

    @Autowired
    public DefaultResultOutputHandler(GameService gameService, PlayerService playerService, PinfallService pinfallService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.pinfallService = pinfallService;
    }

    @Override
    public void printGameResult() {
        System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "FRAME", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        final Map<Player, List<Frame>> framesByPlayer = playerService.getFramesByPlayer();
        framesByPlayer.forEach((player, frames) -> {
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", player.getName(), "", "", "", "", "", "", "", "", "", "");
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Pinfalls", getFramePinfallsOutput(frames.get(0)), getFramePinfallsOutput(frames.get(1)), getFramePinfallsOutput(frames.get(2)), getFramePinfallsOutput(frames.get(3)), getFramePinfallsOutput(frames.get(4)), getFramePinfallsOutput(frames.get(5)), getFramePinfallsOutput(frames.get(6)), getFramePinfallsOutput(frames.get(7)), getFramePinfallsOutput(frames.get(8)), getLastFrameOutput(frames.get(9)));
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s %10s %10s\n", "Score", getScoreOutput(frames.get(0)), getScoreOutput(frames.get(1)), getScoreOutput(frames.get(2)), getScoreOutput(frames.get(3)), getScoreOutput(frames.get(4)), getScoreOutput(frames.get(5)), getScoreOutput(frames.get(6)), getScoreOutput(frames.get(7)), getScoreOutput(frames.get(8)), getLastScoreOutput(frames.get(9)));
        });
    }

    private String getScoreOutput(Frame frame) {
        return frame.getScore() + "  ";
    }

    private String getLastScoreOutput(Frame frame) {
        return frame.getScore() + "    ";
    }

    @Override
    public void finishGame() {
        if (!gameService.gameHasFinished()) {
            throw new BowlingApplicationException("There isn't enough data to finish the game. Please check the the file.");
        }
        gameService.setFinalScores();
    }

    @Override
    public String getFramePinfallsOutput(final Frame frame) {
        final List<Pinfall> pinfalls = pinfallService.findByFrame(frame);
        if (pinfalls.size() == 1) {
            return "    X";
        }
        final int firstShoot = pinfalls.get(0).getQuantity();
        final int secondShoot = pinfalls.get(1).getQuantity();
        if (firstShoot + secondShoot == 10) {
            return " " + firstShoot + "  /";
        }
        return " " + getPinfallQuantityOutput(firstShoot) + "  " + getPinfallQuantityOutput(secondShoot);
    }

    private String getPinfallQuantityOutput(int shootQuantity) {
        return shootQuantity == 10 ? "X" : String.valueOf(shootQuantity);
    }

    @Override
    public String getLastFrameOutput(final Frame lastFrame) {
        String result = getFramePinfallsOutput(lastFrame);
        final List<Pinfall> lastFramePinfalls = pinfallService.findByFrame(lastFrame);
        if (lastFramePinfalls.size() == 3) {
            result = result + "  " + getPinfallQuantityOutput(lastFramePinfalls.get(2).getQuantity());
        }
        return result;
    }
}
