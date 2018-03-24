package replace.data;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Replacers {
    private final ImmutableSet<Replacer> replacers;

    public Replacers(Collection<? extends Replacer> replacers) {
        this.replacers = ImmutableSet.copyOf(replacers);
    }

    public List<String> getWords() {
        return replacers.stream().map(e -> e.getWord()).collect(Collectors.toList());
    }

    public Replacement replace(Replacement text) {
        for (Replacer r : replacers) {
            text = r.replace(text);
        }
        return text;
    }

    public String replace(String text) {
        for (Replacer r : replacers) {
            text = r.replace(text);
        }
        return text;
    }

//	public int countReferences(String text) {
//		return replacers.stream().mapToInt(e->e.countReferences(text)).sum();
//	}
//	
//	public int countDifferentReferences(String text) {
//		return (int) replacers.stream().mapToInt(e->e.countReferences(text)).filter(e->e>0).count();
//	}

}
