package be.thomaswinters.textfixers;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class CompositeFixer implements ISentenceFixer {

	private final ImmutableList<ISentenceFixer> fixers;

	public CompositeFixer(List<? extends ISentenceFixer> fixers) {
		this.fixers = ImmutableList.copyOf(fixers);
	}

	public CompositeFixer(ISentenceFixer... fixers) {
		this(Arrays.asList(fixers));
	}

	@Override
	public String fix(String text) {
		return fixers.stream().reduce(text, (e, f) -> f.fix(e), (e, f) -> e);
	}

}
