package day_9;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Character.getNumericValue;

public class DayNine {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        String input = FileUtil.readToString("day_9.txt");


        System.out.println("Solution for part one is: ");
        System.out.println("Solution for part two is: ");
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @UtilityClass
    static class ChecksumCalculator {

        public static long calculateChecksum(List<Integer> numbers) {
            long checksum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                checksum += (long) i * numbers.get(i);
            }
            return checksum;
        }
    }

    @UtilityClass
    static class InputParser {

        public static List<Integer> parse(String input) {
            int[] diskMap = input.chars().map(Character::getNumericValue).toArray();
            int tail = diskMap.length - 1;
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < diskMap.length && i < tail; i++) {
                int amountOfValuesToAdd = diskMap[i];
                if (i % 2 == 0) {
                    // values to add
                    for (int j = 0; j < amountOfValuesToAdd; j++) {
                        result.add(i / 2);
                    }
                } else {
                    // gaps to fill
                    int amountOfFillers = diskMap[tail];
                    for (int j = 0; j < amountOfValuesToAdd; j++) {
                        if (amountOfFillers == 0 && diskMap[tail] != 0) {
                            diskMap[tail] = 0;
                        }
                        while (amountOfFillers == 0) {
                            tail -= 2;
                            amountOfFillers = diskMap[tail];
                        }
                        if (tail <= i) {
                            break;
                        }
                        result.add(tail / 2);
                        amountOfFillers--;
                    }
                    if (amountOfFillers > 0) {
                        diskMap[tail] = amountOfFillers;
                    }
                }
            }
            return result;
        }
    }
}
