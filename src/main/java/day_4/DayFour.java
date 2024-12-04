package day_4;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayFour {

    public static void main(String[] args) throws FileNotFoundException {
        WordSearch wordSearch = InputParser.parse(FileUtil.readToList("day_4.txt"));
        System.out.println("Solution for part one is: " + wordSearch.countXmas());
        System.out.println("Solution for part two is: " + wordSearch.countCrossMas());
    }

    record WordSearch(List<String> input, List<String> lines) {

        private static final Pattern XMAS_PATTERN = Pattern.compile("XMAS");
        private static final Pattern SAMX_PATTERN = Pattern.compile("SAMX");

        public int countXmas() {
            return lines.stream()
                    .map(WordSearch::countXmas)
                    .reduce(Integer::sum)
                    .orElseThrow();
        }

        private static int countXmas(String line) {
            Matcher xmas = XMAS_PATTERN.matcher(line);
            Matcher samx = SAMX_PATTERN.matcher(line);
            return (int) (xmas.results().count() + samx.results().count());
        }

        public int countCrossMas() {
            int count = 0;
            for (int y = 1; y < input.size() - 1; y++) {
                for (int x = 1; x < input.get(y).length() - 1; x++) {
                    if (isCrossMas(x, y)) {
                        count++;
                    }
                }
            }
            return count;
        }

        private boolean isCrossMas(int x, int y) {
            if (input.get(y).charAt(x) != 'A') {
                return false;
            }
            char topLeft = input.get(y - 1).charAt(x - 1);
            char topRight = input.get(y - 1).charAt(x + 1);
            char bottomLeft = input.get(y + 1).charAt(x - 1);
            char bottomRight = input.get(y + 1).charAt(x + 1);
            return ((topLeft == 'M' && bottomRight == 'S') || (topLeft == 'S' && bottomRight == 'M')) &&
                   ((topRight == 'M' && bottomLeft == 'S') || (topRight == 'S' && bottomLeft == 'M'));
        }
    }

    @UtilityClass
    static class InputParser {

        public static WordSearch parse(List<String> input) {
            int height = input.size();
            int width = input.get(0).length();
            List<String> result = new ArrayList<>(input);
            for (int x = 0; x < width; x++) {
                StringBuilder verticalLine = new StringBuilder();
                for (String line : input) {
                    verticalLine.append(line.charAt(x));
                }
                result.add(verticalLine.toString());
            }
            for (int x = 0; x < width; x++) {
                int pointer = x;
                StringBuilder diagonalLine = new StringBuilder();
                for (int y = 0; y < height && pointer >= 0; y++) {
                    diagonalLine.append(input.get(y).charAt(pointer--));
                }
                result.add(diagonalLine.toString());
            }
            for (int y = 1; y < height; y++) {
                int pointer = y;
                StringBuilder diagonalLine = new StringBuilder();
                for (int x = width - 1; x >= 0 && pointer < height; x--) {
                    diagonalLine.append(input.get(pointer++).charAt(x));
                }
                result.add(diagonalLine.toString());
            }
            for (int x = width - 1; x >= 0; x--) {
                int pointer = x;
                StringBuilder diagonalLine = new StringBuilder();
                for (int y = 0; y < height && pointer < width; y++) {
                    diagonalLine.append(input.get(y).charAt(pointer++));
                }
                result.add(diagonalLine.toString());
            }
            for (int y = 1; y < height; y++) {
                int pointer = y;
                StringBuilder diagonalLine = new StringBuilder();
                for (int x = 0; x < width && pointer < height; x++) {
                    diagonalLine.append(input.get(pointer++).charAt(x));
                }
                result.add(diagonalLine.toString());
            }
            return new WordSearch(input, result);
        }
    }
}
