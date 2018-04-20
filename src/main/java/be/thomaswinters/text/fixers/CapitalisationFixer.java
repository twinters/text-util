package be.thomaswinters.text.fixers;

public class CapitalisationFixer implements ISentenceFixer {

	/**
	 * Algorithm from
	 * https://stackoverflow.com/questions/16078479/capitalize-first-word-of-a-sentence-in-a-string-with-multiple-sentences
	 */
	@Override
	public String fix(String text) {
		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(text);
		while (pos < sb.length()) {
			if (sb.charAt(pos) == '.') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
				sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
				capitalize = false;
			}
			pos++;
		}
		return sb.toString();
	}
}
