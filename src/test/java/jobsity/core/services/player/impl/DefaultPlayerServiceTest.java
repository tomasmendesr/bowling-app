package jobsity.core.services.player.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Player;
import jobsity.core.repositories.FrameRepository;
import jobsity.core.repositories.PlayerRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPlayerServiceTest {

    private PlayerRepository playerRepository;
    private FrameRepository frameRepository;
    private DefaultPlayerService playerService;

    @Before
    public void setUp() {
        playerRepository = mock(PlayerRepository.class);
        frameRepository = mock(FrameRepository.class);
        playerService = new DefaultPlayerService(playerRepository, frameRepository);
    }

    @Test
    public void testGetFramesByPlayer() {
        Player somePlayer = new Player();
        somePlayer.setName("Tomas");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(somePlayer));
        when(frameRepository.findByPlayer(somePlayer)).thenReturn(Arrays.asList(mock(Frame.class), mock(Frame.class)));
        Map result = playerService.getFramesByPlayer();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, ((List<Frame>) result.get(somePlayer)).size());
    }
}
