package be.thomaswinters.textgenerator.fixers;

import java.util.Arrays;
import java.util.List;

public class MaxLineLengthFixer implements ISentenceFixer {

	private final int maxLength;

	public MaxLineLengthFixer(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public String fix(String text) {
		List<String> lines = Arrays.asList(text.split("\n"));
		StringBuilder b = new StringBuilder();
		for (String line : lines) {
			while (line.length() > maxLength) {
				int lastSpace = line.substring(0, maxLength).lastIndexOf(" ");
				if (lastSpace > 0) {
					b.append(line.substring(0, lastSpace) + "\n");
				} else {
					b.append(line + "\n");
					break;
				}
				line = line.substring(lastSpace).trim();
			}
			b.append(line + "\n");

		}

		return b.toString().trim();
	}

}
