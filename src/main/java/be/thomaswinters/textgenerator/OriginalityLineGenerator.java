package be.thomaswinters.textgenerator;

import java.util.Arrays;
import java.util.Collection;

/**
 * Same as originality text generator, but checks for every line if it's
 * original.
 * 
 * @author Thomas Winters
 *
 */
public class OriginalityLineGenerator extends OriginalityTextGenerator {

	public OriginalityLineGenerator(Collection<String> originalLines, ITextGenerator generator) {
		super(originalLines, generator);
	}

	@Override
	public boolean isOriginal(String text) {
		if (!text.contains("\n")) {
			return super.isOriginal(text);
		} else {
			// return
			// Arrays.asList(text.split("\n")).stream().anyMatch(this::isOriginal);
			return Arrays.asList(text.split("\n")).stream().mapToInt(e -> isOriginal(e) ? 1 : -1).sum() > 0;
		}
	}
}
