package jobsity.core.services.frame.frameinputhandler.impl;

import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.services.frame.FrameService;
import jobsity.core.utils.inputreadervalidator.FrameLineValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DefaultFrameInputHandlerTest {

    @Mock
    FrameService frameService;

    @Mock
    FrameLineValidator frameLineValidator;

    @InjectMocks
    DefaultFrameInputHandler frameInputHandler;

    @Before
    public void setUp() {
        this.frameService = mock(FrameService.class);
        this.frameLineValidator = mock(FrameLineValidator.class);
        frameInputHandler = new DefaultFrameInputHandler(frameLineValidator, frameService);
    }

    @Test(expected = BowlingApplicationException.class)
    public void testHandle_whenFrameLineValidatorThrowsException() {
        doThrow(new BowlingApplicationException("test")).when(frameLineValidator).validateFrameLine(anyString());
        frameInputHandler.validateInput("some input");
    }

    @Test
    public void testGetPlayerNameFromInput() {
        assertEquals("John", frameInputHandler.getPlayerNameFromInput("John F"));
    }

    @Test
    public void testGetPinfallsFromInput_whenIsAnF() {
        assertEquals(0, frameInputHandler.getPinfallsFromInput("John F"));
    }

    @Test
    public void testGetPinfallsFromInput_whenIsANumber() {
        assertEquals(3, frameInputHandler.getPinfallsFromInput("John 3"));
    }

    @Test
    public void testSplitInput() {
        String someString = "John 29";
        String[] result = frameInputHandler.splitInput(someString);
        assertEquals(2, result.length);
    }
}
