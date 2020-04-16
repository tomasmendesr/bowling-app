package jobsity.core.services.pinfall.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.entities.Player;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.repositories.PinfallRepository;
import jobsity.core.services.frame.FrameService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPinfallServiceTest {

    PinfallRepository pinfallRepository;
    DefaultPinfallService defaultPinfallService;

    @Before
    public void setUp() {
        this.pinfallRepository = mock(PinfallRepository.class);
        this.defaultPinfallService = new DefaultPinfallService(pinfallRepository);
    }

    @Test(expected = BowlingApplicationException.class)
    public void testValidateNewPinfallInFrame_whenIsInvalid() {
        Player player = new Player();
        player.setName("TestName");

        Frame frame = new Frame();
        frame.setFrameNumber(5);
        frame.setPlayer(player);

        Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(8);
        when(pinfallRepository.findByFrame(frame)).thenReturn(Arrays.asList(pinfall));

        Pinfall newPinfall = new Pinfall();
        newPinfall.setQuantity(3);
        defaultPinfallService.validateNewPinfallInFrame(frame, newPinfall);
    }

    @Test
    public void testValidateNewPinfallInFrame_whenIsOk() {
        Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(8);
        when(pinfallRepository.findByFrame(mock(Frame.class))).thenReturn(Arrays.asList(pinfall));

        Pinfall newPinfall = new Pinfall();
        newPinfall.setQuantity(2);
        defaultPinfallService.validateNewPinfallInFrame(mock(Frame.class), newPinfall);
    }
}
