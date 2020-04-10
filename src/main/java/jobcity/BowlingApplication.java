package jobcity;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.services.frame.frameinputhandler.FrameInputHandler;
import jobcity.core.services.resultoutputhandler.ResultOutputHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class BowlingApplication {

    @Autowired
    private static FrameInputHandler frameInputHandler;

    @Autowired
    private static ResultOutputHandler resultOutputHandler;

    public static void main (String[] args) {
        System.out.println("BowlingApp has started! \n \n");

        try {
            if (args.length != 1) {
                throw new WrongNumberArgsException("file.txt");
            }

            final File file = new File(args[0]);
            int dotIndex = file.getName().lastIndexOf('.');
            String fileExtension = (dotIndex == -1) ? "" : file.getName().substring(dotIndex + 1);
            if (!"txt".equals(fileExtension)) {
                throw new IOException("The file should have a .txt extension");
            }

            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String readLine;
            while (Objects.nonNull(readLine = bufferedReader.readLine())) {
                frameInputHandler.handle(readLine);
            }

            resultOutputHandler.printGameResult();
        } catch (WrongNumberArgsException wrongArgsExceptions) {
            System.out.println("A .txt file is required as input. \n");
            printFileFormatInformation();
        } catch (IOException ioeException) {
            System.out.println("There was an error reading the file. \n");
            printFileFormatInformation();
        } catch (BowlingApplicationException bowlingApplicationException) {
            System.out.println(bowlingApplicationException.getMessage());
        }

        System.exit(0);
    }

    private static void printFileFormatInformation() {
        System.out.println("The expected format is the following: \n");
        System.out.println("Each line represents a player and a chance with the subsequent number of pins\n" +
                "knocked down.\n" +
                "An 'F' indicates a foul on that chance and no pins knocked down (identical for\n" +
                "scoring to a roll of 0).\n \n");
        System.out.println("Example: \n");
        System.out.println("Jeff 10\n" +
                "John 3\n" +
                "John F\n");
    }
}
