package day_5;

import day_5.DayFive.InputParser;
import day_5.DayFive.Page;
import day_5.DayFive.Update;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DayFiveTest {

    public static final List<String> INPUT = Arrays.asList(
            "47|53",
            "97|13",
            "97|61",
            "97|47",
            "75|29",
            "61|13",
            "75|53",
            "29|13",
            "97|29",
            "53|29",
            "61|53",
            "97|53",
            "61|29",
            "47|13",
            "75|47",
            "97|75",
            "47|61",
            "75|61",
            "47|29",
            "75|13",
            "53|13",
            "",
            "75,47,61,53,29",
            "97,61,53,29,13",
            "75,29,13",
            "75,97,47,61,53",
            "61,13,29",
            "97,13,75,29,47"
    );

    @Test
    void should_parse_pages() {
        Map<Integer, Page> pageMap = InputParser.parse(INPUT).pageMap();
        assertThat(pageMap.get(47).getAfter()).map(Page::getPageNumber).containsExactly(97, 75);
        assertThat(pageMap.get(47).getBefore()).map(Page::getPageNumber).containsExactly(53, 13, 61, 29);
    }

    @Test
    void should_parse_updates() {
        List<Update> updates = InputParser.parse(INPUT).updates();
        assertThat(updates).hasSize(6);
        assertThat(updates.get(1).getPages()).map(Page::getPageNumber).containsExactly(97, 61, 53, 29, 13);
    }

    @Test
    void should_collect_correctly_ordered_updates() {
        List<Update> updates = InputParser.parse(INPUT).collectCorrectlyOrderedUpdates();
        assertThat(updates)
                .map(Update::getPages)
                .map(pages -> pages.stream().map(Page::getPageNumber).toList())
                .containsExactly(
                        Arrays.asList(75, 47, 61, 53, 29),
                        Arrays.asList(97, 61, 53, 29, 13),
                        Arrays.asList(75, 29, 13)
                );
    }

    @Test
    void should_sum_correctly_ordered_middle_page_numbers() {
        assertThat(InputParser.parse(INPUT).sumCorrectlyOrderedMiddlePageNumbers()).isEqualTo(143);
    }

    @Test
    void should_collect_fixed_incorrectly_ordered_updates() {
        List<Update> updates = InputParser.parse(INPUT).collectFixedIncorrectlyOrderedUpdates();
        assertThat(updates)
                .map(Update::getPages)
                .map(pages -> pages.stream().map(Page::getPageNumber).toList())
                .containsExactly(
                        Arrays.asList(97, 75, 47, 61, 53),
                        Arrays.asList(61, 29, 13),
                        Arrays.asList(97, 75, 47, 29, 13)
                );
    }

    @Test
    void should_sum_fixed_incorrectly_ordered_middle_page_numbers() {
        assertThat(InputParser.parse(INPUT).sumFixedIncorrectlyOrderedMiddlePageNumbers()).isEqualTo(123);
    }
}