package be.thomaswinters.textfixers;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Fixes a sentence by applying a fixer on every rule (seperated by a
 * linebreak), rather than on the whole string
 * 
 * @author Thomas Winters
 *
 */
public class RuleFixer implements ISentenceFixer {

	private final ISentenceFixer internalFixer;

	public RuleFixer(ISentenceFixer internalFixer) {
		super();
		this.internalFixer = internalFixer;
	}

	@Override
	public String fix(String text) {
		return Arrays.asList(text.split("\n")).stream().map(e -> internalFixer.fix(e))
				.collect(Collectors.joining("\n"));
	}

}
