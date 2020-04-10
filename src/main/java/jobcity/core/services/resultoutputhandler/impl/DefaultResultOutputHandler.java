package jobcity.core.services.resultoutputhandler.impl;

import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.services.game.GameService;
import jobcity.core.services.resultoutputhandler.ResultOutputHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultResultOutputHandler implements ResultOutputHandler {

    @Autowired
    private GameService gameService;


    @Override
    public void printGameResult() {
        if (!gameService.gameHasFinished()) {
            throw new BowlingApplicationException("There isn't enought data to finish the game. Please check the the file.");
        }

        // TODO calcular scores de frames por jugador
        // Calcular score total por jugador
    }
}
