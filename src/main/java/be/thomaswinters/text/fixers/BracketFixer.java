package be.thomaswinters.text.fixers;

import java.util.OptionalInt;
import java.util.Stack;
import java.util.regex.Pattern;

import be.thomaswinters.sentence.SentenceUtil;

import org.apache.commons.lang3.StringUtils;

public class BracketFixer implements ISentenceFixer {
	private final String openBracket;
	private final String closeBracket;

	public BracketFixer(String openBracket, String closeBracket) {
		this.openBracket = openBracket;
		this.closeBracket = closeBracket;
	}

	@Override
	public String fix(String text) {
		return fixBrackets(text);
	}

	/*-********************************************-*
	 *  Bracket balancing
	*-********************************************-*/

	/**
	 * Checks if a text has balanced brackets
	 * 
	 * @param text
	 * @param openBracket
	 * @param closeBracket
	 * @return
	 */
	public static boolean hasBalancedBrackets(String text, String openBracket, String closeBracket) {
		String current = text.replaceAll("[^" + openBracket + closeBracket + "]", "");

		String previous = "";
		while (current.length() != previous.length()) {
			previous = current;
			current = current.replace(openBracket + closeBracket, "");
		}
		return current.length() == 0;
	}

	public static String fixSpeechBrackets(String text) {
		return fixBrackets(text, "\"", "\"");
	}

	/**
	 * Replaces brackets that are non problematic (e.g. balanced) with a space
	 * 
	 * @param text
	 * @return
	 */
	public String replaceNonProblematicBrackets(String text) {
		Stack<Integer> openBrackets = new Stack<>();

		int nextOpenBracket = text.indexOf(openBracket);
		if (nextOpenBracket >= 0) {
			openBrackets.push(nextOpenBracket);
		}
		int nextClosedBracket = text.indexOf(closeBracket);

		while (!openBrackets.isEmpty() && nextClosedBracket >= 0) {

			int relativeNewFirstOpenBracket = text.substring(openBrackets.peek() + 1).indexOf(openBracket);
			int nextFirstOpenBracket = relativeNewFirstOpenBracket < 0 ? relativeNewFirstOpenBracket
					: openBrackets.peek() + relativeNewFirstOpenBracket + 1;

			if (nextFirstOpenBracket >= 0 && nextFirstOpenBracket < nextClosedBracket) {
				openBrackets.push(nextFirstOpenBracket);
			} else if (!openBrackets.isEmpty()) {

				// If the next open is after the next close, process rest
				if (openBrackets.peek() > nextClosedBracket) {
					return text.substring(0, nextClosedBracket + 1)
							+ replaceNonProblematicBrackets(text.substring(nextClosedBracket + 1));
				}

				text = SentenceUtil.replaceCharacters(text, openBrackets.pop(), openBracket, ' ');
				text = SentenceUtil.replaceCharacters(text, nextClosedBracket, closeBracket, ' ');

				if (openBrackets.isEmpty()) {
					return text.substring(0, nextClosedBracket)
							+ replaceNonProblematicBrackets(text.substring(nextClosedBracket));
				}
				nextClosedBracket = text.indexOf(closeBracket);
			} else {
				return text;
			}
		}

		return text;
	}

	public static String fixBrackets(String text, String openBracket, String closedBracket) {
		return new BracketFixer(openBracket, closedBracket).fixBrackets(text);
	}

	public String fixBrackets(String text) {

		// Check if the balanced brackets
		if (hasBalancedBrackets(text, openBracket, closeBracket)) {
			return text;
		}

		// Check if the brackets are the same: different algorithm
		if (openBracket.equals(closeBracket)) {
			return fixSameBrackets(text, openBracket);
		}

		String filteredText = replaceNonProblematicBrackets(text);

		// Fix brackets
		int nextOpenBracket = filteredText.indexOf(openBracket);
		int nextClosedBracket = filteredText.indexOf(closeBracket);

		// If there exists a closed bracket
		if (nextClosedBracket >= 0) {

			// If first closed bracket is before any open brackets
			if (nextClosedBracket >= 0 && (nextOpenBracket < 0 || nextClosedBracket < nextOpenBracket)) {
				int previousSentenceEnd = SentenceUtil.getLastSentenceEndIndex(text.substring(0, nextClosedBracket > 0 ? nextClosedBracket - 1 : nextClosedBracket))
						.orElse(0);

				// Fix punctuation start
				while (SentenceUtil.isPunctuation(text.charAt(previousSentenceEnd))
						|| text.charAt(previousSentenceEnd) == ' ') {
					previousSentenceEnd += 1;
				}
				text = text.substring(0, previousSentenceEnd) + openBracket + text.substring(previousSentenceEnd);

				return fixBrackets(text);
			}
		}
		// There isn't a closing bracket but there is an opening bracket
		else if (nextOpenBracket >= 0) {
			OptionalInt nextSentenceEndOptional = SentenceUtil
					.getFirstSentenceEndIndex(text.substring(nextOpenBracket));
			int nextSentenceEnd = nextSentenceEndOptional.isPresent()
					? nextOpenBracket + nextSentenceEndOptional.getAsInt() : text.length();

			text = text.substring(0, nextSentenceEnd) + closeBracket + text.substring(nextSentenceEnd);
			return fixBrackets(text);

		}

		// Give up, replace them all
		return text.replaceAll(Pattern.quote(openBracket), "").replaceAll(Pattern.quote(closeBracket), "");

	}

	/**
	 * Algorithm to fix brackets if the opening and closing brackets are the
	 * same
	 * 
	 * @param text
	 *            The text to fix
	 * @param tag
	 *            The bracket
	 * @return
	 */
	public static String fixSameBrackets(String text, String tag) {

		// Check if the brackets are balanced
		int amountOfBrackets = StringUtils.countMatches(text, tag);
		if (amountOfBrackets % 2 == 1) {
			// Nope, an uneven amount means the brackets are not balanced

			// Easy case: If there's only one bracket
			if (amountOfBrackets == 1) {
				// If there is only one, check if it starts or ends one
				int speechTagIndex = text.lastIndexOf(tag);

				if (speechTagIndex == 0) {
					// Just a speech tag in the beginning: remove.
					return text.substring(1);
				} else if (speechTagIndex == text.length() - 1) {
					// Just a speech tag in the end: remove.
					return text.substring(0, text.length() - 1);
				}
				if (Character.isAlphabetic(text.charAt(speechTagIndex - 1))
						|| SentenceUtil.isPunctuation(text.charAt(speechTagIndex - 1))) {
					// It's at the end of a word, add a bracket in the beginning
					// of the sentence.
					OptionalInt lastSentenceEnd = SentenceUtil
							.getLastSentenceEndIndex(text.substring(0, speechTagIndex - 1));

					if (lastSentenceEnd.isPresent()) {
						// Skip the space: do +2 instead of +1
						return text.substring(0, lastSentenceEnd.getAsInt() + 1) + " " + tag
								+ text.substring(lastSentenceEnd.getAsInt() + 2, text.length());
					} else {
						return tag + text;
					}
				} else {
					/*
					 * The tag is at the beginning of a word: Find the next
					 * ending of a sentence.
					 */
					String nextText = text.substring(speechTagIndex);
					OptionalInt nextSentenceEnd = SentenceUtil.getFirstSentenceEndIndex(nextText);

					if (nextSentenceEnd.isPresent()) {

						int nextSentenceEndInt = nextSentenceEnd.getAsInt();

						// If this is at the beginning of a sentence:
						if (SentenceUtil.isPunctuation(text.charAt(speechTagIndex - 2))) {

							String result = text.substring(0, speechTagIndex)
									+ nextText.substring(0, nextSentenceEndInt + 1) + tag
									+ nextText.substring(nextSentenceEndInt + 1, nextText.length());

							return result;
						} else {
							String result = text.substring(0, speechTagIndex)
									+ nextText.substring(0, nextSentenceEndInt) + tag
									+ nextText.substring(nextSentenceEndInt, nextText.length());

							return result;
						}

					} else {
						return text + tag;
					}
				}

			}

			// if (lastDotIndex)

			else {
				// Last resort: Remove all speech tags
				return text.replaceAll(tag, "");
			}
		}

		return text;
	}

	/*-********************************************-*/

}
