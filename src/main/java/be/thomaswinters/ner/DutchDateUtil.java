package be.thomaswinters.ner;

import com.google.common.collect.ImmutableList;
import replace.data.Replacer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DutchDateUtil {
    public static final List<String> months = ImmutableList.copyOf(Arrays.asList("januari", "februari", "maart",
            "april", "mei", "juni", "juli", "augustus", "september", "oktober", "november", "december"));
    private static final String monthsRegexString = "(" + months.stream().collect(Collectors.joining("|")) + ")";

//	public static final Pattern dayMonthPattern = Pattern.compile("\\d+ " + monthsRegexString,
//			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//	public static final Pattern monthPattern = Pattern.compile(monthsRegexString,
//			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//	public static final Pattern yearPattern = Pattern.compile("20\\d\\d",
//			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

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


    public static void main(String[] args) {
        System.out.println(replaceAllDates("April is het nu niet. Het is nu 51 JULI 2014 en morgen is het 4 juli 2099 2014", LocalDateTime.now()));
    }
}
