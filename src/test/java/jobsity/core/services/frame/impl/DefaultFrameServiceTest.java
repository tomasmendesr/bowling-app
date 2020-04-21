package jobsity.core.services.frame.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.repositories.FrameRepository;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.services.player.PlayerService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultFrameServiceTest {

    PinfallService pinfallService;
    PlayerService playerService;
    FrameRepository frameRepository;
    DefaultFrameService frameService;

    @Before
    public void setUp() {
        this.frameRepository = mock(FrameRepository.class);
        this.playerService = mock(PlayerService.class);
        this.pinfallService = mock(PinfallService.class);
        this.frameService = new DefaultFrameService(playerService, pinfallService, frameRepository);
    }

    @Test
    public void testGetCurrentFrame_whenFrameReceivedIsComplete() {
        Frame frame = new Frame();
        frame.setId(2l);
        frame.setFrameNumber(2);
        when(pinfallService.findByFrame(frame)).thenReturn(mockFrameCompletePinfalls());

        Frame nextFrame = new Frame();
        nextFrame.setFrameNumber(3);
        when(frameRepository.save(any(Frame.class))).thenReturn(nextFrame);
        assertEquals(3, frameService.getCurrentFrame(frame).getFrameNumber());
    }

    @Test
    public void testGetCurrentFrame_whenFrameReceivedIsntComplete() {
        Frame frame = new Frame();
        frame.setId(2l);
        frame.setFrameNumber(2);
        when(pinfallService.findByFrame(frame)).thenReturn(mockFrameIncompletePinfalls());
        assertEquals(new Long(2), frameService.getCurrentFrame(frame).getId());
    }

    @Test
    public void testGetLastFrame() {
        Frame frame = new Frame();
        frame.setFrameNumber(2);
        frame.setId(2l);

        Frame anotherFrame = new Frame();
        frame.setId(1l);
        frame.setFrameNumber(9);

        assertEquals(new Long(1), frameService.getLastFrame(Arrays.asList(frame, anotherFrame)).getId());
    }

    private List<Pinfall> mockFrameIncompletePinfalls() {
        Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(7);
        return Arrays.asList(pinfall);
    }

    private List<Pinfall> mockFrameCompletePinfalls() {
        Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(10);
        return Arrays.asList(pinfall);
    }
}
