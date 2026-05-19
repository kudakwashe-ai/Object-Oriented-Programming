public class VowelCounter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Vowels";
    }

    @Override
    public Object execute(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            char ch = Character.toLowerCase(sentence.charAt(i));
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }
        return count;
    }
}
