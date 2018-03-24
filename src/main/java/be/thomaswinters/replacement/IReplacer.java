package be.thomaswinters.replacement;

public interface IReplacer {
    Replacement replace(Replacement replacement);

    String replace(String text);
}
