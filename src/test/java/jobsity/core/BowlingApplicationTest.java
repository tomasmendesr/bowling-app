package jobsity.core;

import jobsity.BowlingApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class BowlingApplicationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMainWhenNoArgsAreReceived(){
        String[] arguments = null;
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] A .txt file is required as arg."));
    }

    @Test
    public void testMainWhenFileIsIncomplete(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/exampleIncomplete.txt").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] There isn't enought data to finish the game. Please check the the file."));
    }

    @Test
    public void testMainWhenFileHasNegativeNumbers(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/exampleWithNegatives.txt").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] The only valid values for pinfalls quantity are 0 to 10 or F in case of being 0."));
    }

    @Test
    public void testMainWhenFileHasAWrongFormat(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/exampleWrongFormat.txt").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] This line: ' Jeff'  is wrong. All rows should have the following format: Player_Name Number_Of_Pinfalls \n" +
                "Number_Of_Pinfalls can be an 'F' if is 0"));
    }

    @Test
    public void testMainWhenFileHasAWrongInput(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/exampleWithWrongInput.txt").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] The only valid values for pinfalls quantity are 0 to 10 or F in case of being 0."));
    }

    @Test
    public void testMainWhenFileIsNotTxt(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/examplePdf.pdf").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("[ERROR] The file should has a .txt extension"));
    }

    @Test
    public void testMainWhenFileIsOk(){
        ClassLoader classLoader = getClass().getClassLoader();
        String[] arguments = new String[1];
        arguments[0] = classLoader.getResource("examples/example.txt").getPath();
        BowlingApplication.main(arguments);
        assertTrue(outContent.toString().contains("  FRAME          1          2          3          4          5          6          7          8          9         10\n" +
                "      Jeff                                                                                                              \n" +
                "  Pinfalls          X       7  /       9  0          X       0  8       8  /       0  6          X          X      X 8 1\n" +
                "     Score                                                                                                              \n" +
                "      John                                                                                                              \n" +
                "  Pinfalls       3  /       6  3          X       8  1          X          X       9  0       7  /       4  4      X 9 0\n" +
                "     Score"));
    }
}
