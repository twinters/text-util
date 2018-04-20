package be.thomaswinters.textfixers;

public class SuffixAdder implements ISentenceFixer {

	public final String suffix;

	public SuffixAdder(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String fix(String text) {
		return text + suffix;
	}

}
