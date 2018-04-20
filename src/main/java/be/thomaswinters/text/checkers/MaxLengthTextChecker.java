package be.thomaswinters.text.checkers;

import java.util.function.Predicate;

public class MaxLengthTextChecker implements Predicate<String> {
	private final int maxLength;

	public MaxLengthTextChecker(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public boolean test(String string) {
		return string.length() <= maxLength;
	}

}
