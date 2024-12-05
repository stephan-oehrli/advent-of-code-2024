package day_5;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;

public class DayFive {

    public static void main(String[] args) throws FileNotFoundException {
        PrintInstruction printInstruction = InputParser.parse(FileUtil.readToList("day_5.txt"));
        System.out.println("Solution for part one is: " + printInstruction.sumCorrectlyOrderedMiddlePageNumbers());
        System.out.println("Solution for part two is: " + printInstruction.sumFixedIncorrectlyOrderedMiddlePageNumbers());
    }

    @RequiredArgsConstructor
    @Getter
    static class Page implements Comparable<Page> {

        private final Integer pageNumber;
        private final List<Page> after = new ArrayList<>();
        private final List<Page> before = new ArrayList<>();

        public void addAfter(Page page) {
            after.add(page);
        }

        public void addBefore(Page page) {
            before.add(page);
        }

        public boolean isBefore(Page page) {
            return before.contains(page);
        }

        @Override
        public int compareTo(Page other) {
            return this.equals(other) ? 0 : (this.isBefore(other) ? -1 : 1);
        }
    }

    static class Update {

        @Getter
        private final List<Page> pages;
        private Boolean isOrderedCorrectly;

        public Update(List<Page> pages) {
            this.pages = new ArrayList<>(pages);
        }

        public boolean isOrderedCorrectly() {
            if (isOrderedCorrectly != null) {
                return isOrderedCorrectly;
            }
            for (int i = 0; i < pages.size() - 1; i++) {
                if (!pages.get(i).isBefore(pages.get(i + 1))) {
                    isOrderedCorrectly = false;
                    return false;
                }
            }
            isOrderedCorrectly = true;
            return true;
        }

        public Update fixOrder() {
            Collections.sort(pages);
            return this;
        }
    }

    record PrintInstruction(Map<Integer, Page> pageMap, List<Update> updates) {

        public List<Update> collectCorrectlyOrderedUpdates() {
            return updates.stream().filter(Update::isOrderedCorrectly).toList();
        }

        public List<Update> collectFixedIncorrectlyOrderedUpdates() {
            return updates.stream()
                    .filter(u -> !u.isOrderedCorrectly())
                    .map(Update::fixOrder)
                    .toList();
        }

        public int sumCorrectlyOrderedMiddlePageNumbers() {
            return sumMiddlePageNumbers(collectCorrectlyOrderedUpdates());
        }

        public int sumFixedIncorrectlyOrderedMiddlePageNumbers() {
            return sumMiddlePageNumbers(collectFixedIncorrectlyOrderedUpdates());
        }

        private static int sumMiddlePageNumbers(List<Update> updates) {
            return updates.stream()
                    .map(Update::getPages)
                    .map(pages -> pages.get((pages.size() / 2)).getPageNumber())
                    .reduce(Integer::sum)
                    .orElseThrow();
        }
    }

    @UtilityClass
    static class InputParser {

        public static PrintInstruction parse(List<String> input) {
            Map<Integer, Page> pageMap = new HashMap<>();
            List<Update> updates = new ArrayList<>();
            for (String line : input) {
                if (line.contains("|")) {
                    String[] split = line.split("\\|");
                    Integer left = Integer.parseInt(split[0]);
                    Integer right = Integer.parseInt(split[1]);
                    pageMap.computeIfAbsent(left, Page::new).addBefore(
                            pageMap.computeIfAbsent(right, Page::new)
                    );
                    pageMap.get(right).addAfter(pageMap.get(left));
                } else if (line.contains(",")) {
                    String[] split = line.split(",");
                    List<Page> pages = Arrays.stream(split)
                            .map(Integer::parseInt)
                            .map(pageMap::get)
                            .toList();
                    updates.add(new Update(pages));
                }
            }
            return new PrintInstruction(pageMap, updates);
        }
    }
}
