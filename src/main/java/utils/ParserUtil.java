package utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ParserUtil {

    public static List<String> splitByWhiteSpace(String input) {
        return Arrays.asList(StringUtils.splitByWholeSeparator(input, null));
    }
    
    public static List<Integer> convertToIntegerList(List<String> inputStrings) {
        List<Integer> result = new ArrayList<>();
        for (String input : inputStrings) {
            result.add(Integer.valueOf(input));
        }
        return result;
    }
}
