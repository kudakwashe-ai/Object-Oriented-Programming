public class SentenceReverser implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Reverse";
    }

    @Override
    public Object execute(String sentence) {
        return new StringBuilder(sentence).reverse().toString();
    }
}
