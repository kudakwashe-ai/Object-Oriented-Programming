public interface SentenceOperation {
    String getLabel();
    Object execute(String sentence);
}
