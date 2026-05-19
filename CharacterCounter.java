public class CharacterCounter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Total Characters";
    }

    @Override
    public Object execute(String sentence) {
        return sentence.length();
    }
}
