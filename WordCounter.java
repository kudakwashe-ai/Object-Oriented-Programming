public class WordCounter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Total Words";
    }

    @Override
    public Object execute(String sentence) {
        String trimmed = sentence.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }
}
