package be.thomaswinters.text.checkers;

import java.util.Arrays;
import java.util.Collection;

/**
 * Same as originality text generator, but checks for every line if it's
 * original, and accepts if more than half is original
 * 
 * @author Thomas Winters
 *
 */
public class OriginalityLineChecker extends OriginalityTextChecker {

	public OriginalityLineChecker(Collection<String> originalLines) {
		super(originalLines);
	}

	@Override
	public boolean test(String text) {
		if (!text.contains("\n")) {
			return super.test(text);
		} else {
			return Arrays.stream(text.split("\n"))
					.mapToInt(e -> test(e) ? 1 : -1)
					.sum() > 0;
		}
	}
}
