package be.thomaswinters.textgenerator;

import java.util.function.Supplier;

public interface ITextGenerator extends Supplier<String> {
	String generateText();
	default String get() {
		return generateText();
	}
}
