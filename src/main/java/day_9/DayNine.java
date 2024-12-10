package day_9;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DayNine {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        String input = FileUtil.readToString("day_9.txt");
        List<Integer> fragmentedByFreeSpace = Fragmentizer.fragmentByFreeSpace(InputParser.parse(input));
        List<Integer> fragmentedByWholeFile = Fragmentizer.fragmentByWholeFile(InputParser.parse(input));
        System.out.println("Solution for part one is: " + ChecksumCalculator.calculateChecksum(fragmentedByFreeSpace));
        System.out.println("Solution for part two is: " + ChecksumCalculator.calculateChecksum(fragmentedByWholeFile));
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @UtilityClass
    static class ChecksumCalculator {

        public static long calculateChecksum(List<Integer> numbers) {
            long checksum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                checksum += Math.multiplyExact((long) i, numbers.get(i));
            }
            return checksum;
        }
    }

    @UtilityClass
    static class Fragmentizer {

        public static List<Integer> fragmentByFreeSpace(int[] diskMap) {
            int tail = diskMap.length - 1;
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < diskMap.length && i <= tail; i++) {
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
                        if (amountOfFillers == 0) {
                            diskMap[tail] = 0;
                            while (amountOfFillers == 0) {
                                tail -= 2;
                                amountOfFillers = diskMap[tail];
                            }
                        }
                        if (tail < i) {
                            break;
                        }
                        result.add(tail / 2);
                        amountOfFillers--;
                    }
                    diskMap[tail] = amountOfFillers;
                }
            }
            return result;
        }

        public static List<Integer> fragmentByWholeFile(int[] diskMap) {
            List<Space> disk = new ArrayList<>();
            for (int i = 0; i < diskMap.length; i++) {
                if (i % 2 == 0) {
                    disk.add(Space.fileOf(diskMap[i], i / 2));
                } else {
                    disk.add(Space.freeSpaceOf(diskMap[i]));
                }
            }
            for (int i = disk.size() - 1; i >= 0; i--) {
                Space file = disk.get(i);
                if (file.isFile()) {
                    for (int j = 0; j < i; j++) {
                        Space space = disk.get(j);
                        if (!space.isFile() && space.getSize() >= file.getSize()) {
                            disk.remove(file);
                            disk.add(j, file);
                            int remainingSpace = space.getSize() - file.getSize();
                            if (remainingSpace == 0) {
                                disk.remove(space);
                            } else {
                                space.setSize(remainingSpace);
                            }
                            disk.add(i, Space.freeSpaceOf(file.getSize()));
                            break;
                        }
                    }
                }
            }
            List<Integer> result = new ArrayList<>();
            for (Space space : disk) {
                for (int i = 0; i < space.getSize(); i++) {
                    result.add(space.getValue());
                }
            }
            return result;
        }
    }

    @AllArgsConstructor
    @Getter
    static class Space {

        @Setter
        private int size;
        private int value;
        private boolean isFile;

        public static Space fileOf(int size, int value) {
            return new Space(size, value, true);
        }

        public static Space freeSpaceOf(int size) {
            return new Space(size, 0, false);
        }
    }

    @UtilityClass
    static class InputParser {

        public static int[] parse(String input) {
            return input.chars().map(Character::getNumericValue).toArray();
        }
    }
}
