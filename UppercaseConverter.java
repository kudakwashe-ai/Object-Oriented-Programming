public class UppercaseConverter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Uppercase";
    }

    @Override
    public Object execute(String sentence) {
        return sentence.toUpperCase();
    }
}
