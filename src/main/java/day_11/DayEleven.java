
package day_11;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import utils.FileUtil;
import utils.ParserUtil;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayEleven {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        StoneLine stoneLine = InputParser.parse(FileUtil.readToString("day_11.txt"));
        System.out.println("Solution for part one is: " + stoneLine.countStones(25));
        System.out.println("Solution for part two is: " + stoneLine.countStones(75));
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @RequiredArgsConstructor
    @Getter
    static class StoneLine {

        private final List<Long> stones;
        private final Map<CountStoneParams, Long> cache = new HashMap<>();

        public long countStones(int blinks) {
            return stones.stream()
                    .map(stone -> countStonesCaching(stone, blinks))
                    .reduce(Long::sum)
                    .orElseThrow();
        }

        private long countStones(CountStoneParams params) {
            if (cache.containsKey(params)) {
                return cache.get(params);
            }
            if (params.blinks() == 1) {
                return params.hasStoneEvenNumberOfDigits() ? 2 : 1;
            }
            int nextBlinks = params.blinks() - 1;
            if (params.hasStoneEvenNumberOfDigits()) {
                Split split = Split.of(params);
                return countStonesCaching(split.left(), nextBlinks) + countStonesCaching(split.right(), nextBlinks);
            }
            return countStonesCaching(params.stone() == 0 ? 1 : params.stone() * 2024, nextBlinks);
        }

        private long countStonesCaching(long stone, int blinks) {
            CountStoneParams params = CountStoneParams.of(stone, blinks);
            long count = countStones(params);
            cache.put(params, count);
            return count;
        }
    }

    record CountStoneParams(long stone, int blinks) {

        public boolean hasStoneEvenNumberOfDigits() {
            return String.valueOf(stone).length() % 2 == 0;
        }

        public static CountStoneParams of(long stone, int blinks) {
            return new CountStoneParams(stone, blinks);
        }
    }

    record Split(long left, long right) {

        public static Split of(CountStoneParams params) {
            String number = String.valueOf(params.stone());
            long left = Long.parseLong(number.substring(0, number.length() / 2));
            long right = Long.parseLong(number.substring(number.length() / 2));
            return new Split(left, right);
        }
    }

    @UtilityClass
    static class InputParser {

        public static StoneLine parse(String input) {
            return new StoneLine(ParserUtil.splitByWhiteSpace(input).stream()
                    .map(Long::parseLong)
                    .toList());
        }
    }
}
