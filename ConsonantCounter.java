public class ConsonantCounter implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Consonants";
    }

    @Override
    public Object execute(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            char ch = sentence.charAt(i);
            if (Character.isLetter(ch)) {
                char lower = Character.toLowerCase(ch);
                if (lower != 'a' && lower != 'e' && lower != 'i' && lower != 'o' && lower != 'u') {
                    count++;
                }
            }
        }
        return count;
    }
}
