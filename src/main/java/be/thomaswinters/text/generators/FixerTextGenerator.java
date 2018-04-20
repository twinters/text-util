package be.thomaswinters.text.generators;

import java.util.List;
import java.util.function.Supplier;

import be.thomaswinters.text.fixers.CompositeFixer;
import be.thomaswinters.text.fixers.ISentenceFixer;

public class FixerTextGenerator implements Supplier<String> {
	private final ISentenceFixer fixer;
	private final Supplier<String> generator;

	public FixerTextGenerator(ISentenceFixer fixer, Supplier<String> generator) {
		this.fixer = fixer;
		this.generator = generator;
	}

	public FixerTextGenerator(List<? extends ISentenceFixer> fixers, Supplier<String> generator) {
		this(new CompositeFixer(fixers), generator);
	}

	@Override
	public String get() {
		return fixer.fix(generator.get());
	}

}
