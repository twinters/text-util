package be.thomaswinters.replacement;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableMultiset.Builder;
import com.google.common.collect.Multiset;

import java.util.function.Function;

public class Replacement {
    private final String text;
    private final ImmutableMultiset<Replacer> replacements;

    public Replacement(String text, Multiset<Replacer> replacements) {
        this.text = text;
        this.replacements = ImmutableMultiset.copyOf(replacements);
    }

    public Replacement(String text) {
        this(text, ImmutableMultiset.<Replacer>builder().build());
    }

    public Replacement createNew(String newText, Replacer replacementDone, int amountOfTimes) {
        if (amountOfTimes > 0) {
            Builder<Replacer> b = ImmutableMultiset.<Replacer>builder();
            b.addAll(this.replacements);
            b.addCopies(replacementDone, amountOfTimes);
            return new Replacement(newText, b.build());
        }
        return this;
    }

    public Replacement applyNew(Function<String, String> mapper) {
        return new Replacement(mapper.apply(getText()), replacements);
    }

    public String getText() {
        return text;
    }

    public int getAmountOfDifferentWordsReplaced() {
        return replacements.entrySet().size();
    }

    public ImmutableMultiset<Replacer> getReplacements() {
        return replacements;
    }

    public int getAmountOfWordsReplaced() {
        return replacements.size();
    }

    @Override
    public String toString() {
        return text + " (" + replacements + ")";
    }

}
