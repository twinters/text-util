package be.thomaswinters.textgenerator;

import java.util.List;

import be.thomaswinters.sentencemarkov.fixers.CompositeFixer;
import be.thomaswinters.sentencemarkov.fixers.ISentenceFixer;

public class FixerTextGenerator implements ITextGenerator {
	private final ISentenceFixer fixer;
	private final ITextGenerator generator;

	public FixerTextGenerator(ISentenceFixer fixer, ITextGenerator generator) {
		this.fixer = fixer;
		this.generator = generator;
	}

	public FixerTextGenerator(List<? extends ISentenceFixer> fixers, ITextGenerator generator) {
		this(new CompositeFixer(fixers), generator);
	}

	@Override
	public String generateText() {
		return fixer.fix(generator.generateText());
	}

}
