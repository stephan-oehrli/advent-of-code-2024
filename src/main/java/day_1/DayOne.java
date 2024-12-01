package day_1;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayOne {

    public static void main(String[] args) throws FileNotFoundException {
        LocationLists locationLists = InputParser.parse(FileUtil.readToList("day_1.txt"));
        int distance = locationLists.calculateDistance();
        System.out.println("Solution for part one is: " + distance);
        int similarityScore = locationLists.calculateSimilarityScore();
        System.out.println("Solution for part two is: " + similarityScore);
    }

    @UtilityClass
    static class InputParser {

        public static LocationLists parse(List<String> input) {
            List<Integer> leftList = new ArrayList<>();
            List<Integer> rightList = new ArrayList<>();
            for (String line : input) {
                String[] values = StringUtils.splitByWholeSeparator(line, StringUtils.SPACE);
                leftList.add(Integer.parseInt(values[0]));
                rightList.add(Integer.parseInt(values[1]));
            }
            return new LocationLists(leftList, rightList);
        }
    }

    record LocationLists(List<Integer> leftList, List<Integer> rightList) {

        public int calculateDistance() {
            int distance = 0;
            List<Integer> sortedLeftList = leftList.stream().sorted().toList();
            List<Integer> sortedRightList = rightList.stream().sorted().toList();
            for (int i = 0; i < sortedLeftList.size(); i++) {
                distance += Math.abs(sortedLeftList.get(i) - sortedRightList.get(i));
            }
            return distance;
        }

        public int calculateSimilarityScore() {
            int similarityScore = 0;
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (Integer rightNumber : rightList) {
                frequencyMap.put(rightNumber, frequencyMap.getOrDefault(rightNumber, 0) + 1);
            }
            for (Integer leftNumber : leftList) {
                similarityScore += leftNumber * frequencyMap.getOrDefault(leftNumber, 0);
            }
            return similarityScore;
        }
    }
}
