package jobsity.core.utils.inputreadervalidator.impl;

import jobsity.core.exceptions.LineValidationException;
import jobsity.core.utils.inputreadervalidator.FrameLineValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DefaultFrameLineValidator implements FrameLineValidator {

    private static final String FAULT = "F";
    private static final String SPACE = " ";

    @Override
    public void validateFrameLine(final String line) {
        if (StringUtils.isEmpty(line)) {
            throw new LineValidationException("There should't be blank lines");
        }

        final String[] splitLine = line.split(SPACE);
        if (splitLine.length != 2) {
            throw new LineValidationException("This line: ' " + line + "' " + " is wrong. All rows should have the following format: Player_Name Number_Of_Pinfalls \n" +
                    "Number_Of_Pinfalls can be an 'F' if is 0");
        }

        final String pinfallsString = splitLine[1];
        if (!isAFault(pinfallsString)) {
            try {
                final int pinfalls = Integer.parseInt(pinfallsString);
                if (pinfalls > 10 || pinfalls < 0) {
                    throw new NumberFormatException("Range exception");
                }
            } catch (NumberFormatException numberFormatException) {
                throw new LineValidationException("The only valid values for pinfalls quantity are 0 to 10 or F in case of being 0.");
            }
        }
    }

    @Override
    public boolean isAFault(final String pinfallsString) {
        return FAULT.equals(pinfallsString);
    }
}
