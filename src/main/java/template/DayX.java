package template;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.List;

public class DayX {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> input = FileUtil.readToList("day_x.txt");

        System.out.println("Solution for part one is: ");
        System.out.println("Solution for part two is: ");
    }

    @UtilityClass
    static class InputParser {

        public static Object parse(List<String> input) {
            return null;
        }
    }
}
