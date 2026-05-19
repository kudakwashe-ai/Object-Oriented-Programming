public class LowercaseConverter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Lowercase";
    }

    @Override
    public Object execute(String sentence) {
        return sentence.toLowerCase();
    }
}
