public class PalindromeChecker implements SentenceOperation {
    @Override
    public String getLabel() {
        return "Palindrome";
    }

    @Override
    public Object execute(String sentence) {
        String cleaned = sentence.replaceAll("\\s+", "").toLowerCase();
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed) ? "Yes" : "No";
    }
}
