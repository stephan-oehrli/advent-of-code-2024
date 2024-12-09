package day_8;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DayEight {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        FrequencyCalculator calculator = InputParser.parse(FileUtil.readToList("day_8.txt"));
        System.out.println("Solution for part one is: " + calculator.calculateAllVisibleAntinodes());
        System.out.println("Solution for part two is: " + calculator.calculateAllVisibleAntinodesWithUpdatedModel());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    record Antinode(int x, int y) {
    }

    record Frequency(int x, int y, Set<Antinode> antinodes, MapDimensions mapDimensions) {

        public void calculateAntinode(Frequency frequency) {
            calculateAntinode(frequency, false);
        }

        private void calculateAntinode(Frequency frequency, boolean isCallback) {
            Antinode antinode = new Antinode(2 * x - frequency.x(), 2 * y - frequency.y());
            mapDimensions.checkOnMap(antinode).ifPresent(antinodes::add);
            if (!isCallback) {
                frequency.calculateAntinode(this, true);
            }
        }

        public void calculateAntinodes(Frequency frequency) {
            calculateAntinodes(frequency, false);
        }

        private void calculateAntinodes(Frequency frequency, boolean isCallback) {
            int factor = 1;
            int vectorX = frequency.x() - x;
            int vectorY = frequency.y() - y;
            Antinode antinode = new Antinode(x + factor * vectorX, y + factor * vectorY);
            while (mapDimensions.isOnMap(antinode)) {
                antinodes.add(antinode);
                factor++;
                antinode = new Antinode(x + factor * vectorX, y + factor * vectorY);
            }
            if (!isCallback) {
                frequency.calculateAntinodes(this, true);
            }
        }
    }

    record MapDimensions(int width, int height) {

        private Optional<Antinode> checkOnMap(Antinode antinode) {
            return isOnMap(antinode) ? Optional.of(antinode) : Optional.empty();
        }

        private boolean isOnMap(Antinode antinode) {
            return antinode.x() >= 0 && antinode.x() < width && antinode.y() >= 0 && antinode.y() < height;
        }
    }

    record FrequencyCalculator(Map<Character, List<Frequency>> frequencyMap) {

        public int calculateAllVisibleAntinodes() {
            calculateAntinodes(false);
            Set<Antinode> visibleAntinodes = frequencyMap.values().stream()
                    .flatMap(Collection::stream)
                    .flatMap(frequency -> frequency.antinodes().stream())
                    .collect(Collectors.toSet());
            return visibleAntinodes.size();
        }

        public int calculateAllVisibleAntinodesWithUpdatedModel() {
            calculateAntinodes(true);
            Set<Antinode> visibleAntinodes = frequencyMap.values().stream()
                    .flatMap(Collection::stream)
                    .flatMap(frequency -> frequency.antinodes().stream())
                    .collect(Collectors.toSet());
            return visibleAntinodes.size();
        }

        private void calculateAntinodes(boolean isUpdatedModel) {
            for (Map.Entry<Character, List<Frequency>> signal : frequencyMap.entrySet()) {
                Queue<Frequency> frequencyQueue = new LinkedList<>(signal.getValue());
                while (!frequencyQueue.isEmpty()) {
                    Frequency frequency = frequencyQueue.poll();
                    for (Frequency otherFrequency : frequencyQueue) {
                        if (isUpdatedModel) {
                            frequency.calculateAntinodes(otherFrequency);
                        } else {
                            frequency.calculateAntinode(otherFrequency);
                        }
                    }
                }
            }
        }
    }

    @UtilityClass
    static class InputParser {

        public static FrequencyCalculator parse(List<String> input) {
            Map<Character, List<Frequency>> frequencyMap = new HashMap<>();
            MapDimensions mapDimensions = new MapDimensions(input.get(0).length(), input.size());
            for (int y = 0; y < input.size(); y++) {
                String line = input.get(y);
                for (int x = 0; x < line.length(); x++) {
                    char frequencySign = line.charAt(x);
                    if (frequencySign != '.') {
                        List<Frequency> frequencies = frequencyMap.getOrDefault(frequencySign, new ArrayList<>());
                        frequencies.add(new Frequency(x, y, new HashSet<>(), mapDimensions));
                        frequencyMap.put(frequencySign, frequencies);
                    }
                }
            }
            return new FrequencyCalculator(frequencyMap);
        }
    }
}
