package be.thomaswinters.textgenerator;

public class MaxLengthTextGenerator implements ITextGenerator {
	private final ITextGenerator generator;

	public MaxLengthTextGenerator(int maxLength, ITextGenerator generator) {
		this.generator = new ValidityCheckerTextGenerator(generator, e -> e.length() <= maxLength);
	}

	@Override
	public String generateText() {
		return generator.generateText();
	}

}
