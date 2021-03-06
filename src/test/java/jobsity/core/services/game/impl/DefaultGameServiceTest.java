package jobsity.core.services.game.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.services.frame.FrameService;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.services.player.PlayerService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultGameServiceTest {

    private PlayerService playerService;
    private PinfallService pinfallService;
    private FrameService frameService;
    private DefaultGameService defaultGameService;

    @Before
    public void setUp() {
        this.playerService = mock(PlayerService.class);
        this.pinfallService = mock(PinfallService.class);
        this.frameService = mock(FrameService.class);
        this.defaultGameService = new DefaultGameService(playerService, pinfallService, frameService);
    }

    @Test
    public void testAreFramesComplete_whenTheyAre9() {
        List<Frame> frames = new ArrayList<>();
        int i;
        for (i = 0; i<9;i++) {
            frames.add(mock(Frame.class));
        }
        assertFalse(defaultGameService.areFramesComplete(frames));
    }

    @Test
    public void testAreFramesComplete_whenTheyAre10() {
        List<Frame> frames = new ArrayList<>();
        int i;
        for (i = 0; i < 10; i++) {
            frames.add(mock(Frame.class));
        }
        assertTrue(defaultGameService.areFramesComplete(frames));
    }

    @Test
    public void testGameHasFinishedWhenSomePlayerDidntFinish() {
        Map<Player, List<Frame>> map = new HashMap<>();
        Player player1 = new Player();
        Player player2 = new Player();
        int i;
        List<Frame> frames = new ArrayList<>();
        for (i = 0; i < 10; i++) {
            frames.add(mock(Frame.class));
        }
        map.put(player1, frames);
        map.put(player2, new ArrayList<>());
        when(playerService.getFramesByPlayer()).thenReturn(map);

        assertFalse(defaultGameService.gameHasFinished());
    }

    @Test
    public void testGameHasFinishedWhenAllPlayersFinished() {
        Map<Player, List<Frame>> map = new HashMap<>();
        Player player1 = new Player();
        Player player2 = new Player();
        int i;
        List<Frame> frames = new ArrayList<>();
        for (i = 0; i < 10; i++) {
            frames.add(mock(Frame.class));
        }
        map.put(player1, frames);
        map.put(player2, frames);
        when(playerService.getFramesByPlayer()).thenReturn(map);

        assertTrue(defaultGameService.gameHasFinished());
    }


}