package jobsity;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.repositories.FrameRepository;
import jobsity.core.repositories.PinfallRepository;
import jobsity.core.repositories.PlayerRepository;
import jobsity.core.services.frame.frameinputhandler.FrameInputHandler;
import jobsity.core.services.resultoutputhandler.ResultOutputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
@Component
@EnableJpaRepositories(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        FrameRepository.class,
                        PlayerRepository.class,
                        PinfallRepository.class
                }
                )}
)
public class BowlingApplication {

    @Autowired
    private FrameInputHandler frameInputHandler;

    @Autowired
    private ResultOutputHandler resultOutputHandler;

    public static void main (String[] args) {
        final ApplicationContext context = new AnnotationConfigApplicationContext(BowlingApplication.class);
        context.getBean(BowlingApplication.class).start(args);
    }

    private void start(String[] args) {
        System.out.println("BowlingApp has started! \n \n");

        try {
            if (args == null || args.length != 1) {
                throw new WrongNumberArgsException("file.txt");
            }
            final File file = new File(args[0]);
            validateFile(file);
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine;
            while (Objects.nonNull(readLine = bufferedReader.readLine())) {
                frameInputHandler.handle(readLine);
            }
            resultOutputHandler.printGameResult();
        } catch (WrongNumberArgsException wrongArgsExceptions) {
            System.out.println("A .txt file is required as arg. \n");
            printFileFormatInformation();
        } catch (IOException ioeException) {
            System.out.println("There was an error reading the file. \n");
            printFileFormatInformation();
        } catch (BowlingApplicationException bowlingApplicationException) {
            System.out.println(bowlingApplicationException.getMessage());
        }

        System.exit(0);
    }

    private static void validateFile(final File file) throws IOException {
        int dotIndex = file.getName().lastIndexOf('.');
        String fileExtension = (dotIndex == -1) ? "" : file.getName().substring(dotIndex + 1);
        if (!"txt".equals(fileExtension)) {
            throw new IOException("The file should have a .txt extension");
        }
    }

    private static void printFileFormatInformation() {
        System.out.println("The expected format is the following:");
        System.out.println("Each line represents a player and a chance with the subsequent number of pins\n" +
                "knocked down.\n" +
                "An 'F' indicates a foul on that chance and no pins knocked down (identical for\n" +
                "scoring to a roll of 0).\n \n");
        System.out.println("Example: \n");
        System.out.println("Jeff 10\n" +
                "John 3\n" +
                "John F\n" +
                "etc...");
    }
}
