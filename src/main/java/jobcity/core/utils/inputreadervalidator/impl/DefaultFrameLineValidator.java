package jobcity.core.utils.inputreadervalidator.impl;

import jobcity.core.exceptions.LineValidationException;
import jobcity.core.utils.inputreadervalidator.FrameLineValidator;
import org.springframework.util.StringUtils;

public class DefaultFrameLineValidator implements FrameLineValidator {

    @Override
    public void validateFrameLine(final String line) {
        if (StringUtils.isEmpty(line)) {
            throw new LineValidationException("There should't be blank lines");
        }

        final String[] splitedLine = line.split(" ");
        if (splitedLine.length != 2) {
            throw new LineValidationException("This line: ' " + line + "' " + " is wrong. All rows should have the following format: Player_Name Number_Of_Pinfalls \n" +
                    "Number_Of_Pinfalls can be an 'F' if is 0");
        }

        final String pinfallsString = splitedLine[1];
        if (!"F".equals(pinfallsString)) {
            try {
                final Integer pinfalls = Integer.parseInt(pinfallsString);
                if (pinfalls > 10 || pinfalls < 1) {
                    throw new NumberFormatException("Range exception");
                }
            } catch (NumberFormatException numberFormatException) {
                throw new LineValidationException("The only valid values for pinfalls quantity are 1 to 10 or F in case of being 0.");
            }
        }
    }
}
