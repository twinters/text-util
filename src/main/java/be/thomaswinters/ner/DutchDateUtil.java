package be.thomaswinters.ner;

import be.thomaswinters.replacement.Replacer;
import com.google.common.collect.ImmutableList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DutchDateUtil {
    public static final List<String> months = ImmutableList.copyOf(Arrays.asList("januari", "februari", "maart",
            "april", "mei", "juni", "juli", "augustus", "september", "oktober", "november", "december"));
    private static final String monthsRegexString = "(" + months.stream().collect(Collectors.joining("|")) + ")";

    public static String replaceAllDates(String text, LocalDateTime date) {
        Replacer dayMonthReplacer = new Replacer("\\d+ " + monthsRegexString, toDayAndMonth(date), false, true);
        Replacer monthReplacer = new Replacer(monthsRegexString, toMonth(date), false, true);
        Replacer yearIntervalReplacer = new Replacer("20\\d\\d\\-20\\d\\d", (toYear(date) - 1) + "-" + toYear(date), false, true);
        Replacer yearReplacer = new Replacer("20\\d\\d", toYear(date) + "", false, true);

        String result = yearReplacer.replace(yearIntervalReplacer.replace(monthReplacer.replace(dayMonthReplacer.replace(text))));

        return result;
    }

    public static String toDayAndMonth(LocalDateTime date) {
        return date.getDayOfMonth() + " " + months.get(date.getMonth().ordinal());
    }

    public static String toMonth(LocalDateTime date) {
        return months.get(date.getMonth().ordinal());
    }

    private static int toYear(LocalDateTime date) {
        return date.getYear();
    }

}
