package be.thomaswinters.textgenerator;

import java.util.function.Function;

public class ValidityCheckerTextGenerator implements ITextGenerator {

	private final ITextGenerator generator;
	private final Function<String, Boolean> checker;

	/**
	 * 
	 * @param generator
	 *            Internal text generator (decorator pattern)
	 * @param checker
	 *            Function returning true if the text is valid
	 */
	public ValidityCheckerTextGenerator(ITextGenerator generator, Function<String, Boolean> checker) {
		super();
		this.generator = generator;
		this.checker = checker;
	}

	@Override
	public String generateText() {
		String generated;
		do {
			generated = generator.generateText();
		} while (!checker.apply(generated));
		return generated;
	}

}
