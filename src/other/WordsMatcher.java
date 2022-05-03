package other;

public class WordsMatcher {
    public static void main(String[] args) {
        //[2,3], ["abc","gdef"]
        int[] digits = {2, 3};
        String[] words = {"abcgz", "defwx"};
        boolean[] response = validWords(digits, words);
        for (int i = 0; i < response.length; i++) {
            System.out.println(response[i]);

        }
    }

    public static boolean[] validWords(int[] digits, String[] words) {
        boolean[] results = new boolean[words.length];
        String[] rightWords = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        String totalValidWords = "";
        for (int i = 0; i < digits.length; i++) {
            totalValidWords += rightWords[digits[i]];
        }
        for (int i = 0; i < words.length; i++) {
            boolean isCharacterContained = true;
            for (int j = 0; j < words[i].length(); j++) {
                isCharacterContained = isCharacterContained && totalValidWords.contains("" + words[i].charAt(j));
                if(!isCharacterContained){
                    break;
                }
            }

            results[i] = isCharacterContained;
        }
        return results;
    }
}
