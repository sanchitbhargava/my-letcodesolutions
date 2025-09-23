import java.util.Arrays;

class Solution {
    /**
     * Calculates the longest common prefix for adjacent strings after removing each element one by one.
     *
     * @param words The input array of strings.
     * @return An array where answer[i] is the max LCP after removing words[i].
     */
    public int[] longestCommonPrefix(String[] words) {
        int n = words.length;
        if (n <= 1) {
            // If there's 0 or 1 word, removing it leaves no adjacent pairs.
            return new int[n];
        }

        // Step 1: Pre-calculate LCPs for all original adjacent pairs.
        int[] lcpValues = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            lcpValues[i] = calculateLcp(words[i], words[i + 1]);
        }

        // Step 2: Create prefix and suffix max arrays on the LCP values.
        int[] prefixMax = new int[n - 1];
        prefixMax[0] = lcpValues[0];
        for (int i = 1; i < n - 1; i++) {
            prefixMax[i] = Math.max(prefixMax[i - 1], lcpValues[i]);
        }

        int[] suffixMax = new int[n - 1];
        suffixMax[n - 2] = lcpValues[n - 2];
        for (int i = n - 3; i >= 0; i--) {
            suffixMax[i] = Math.max(suffixMax[i + 1], lcpValues[i]);
        }

        // Step 3: Calculate the answer for each removal.
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            // Find the maximum LCP from the pairs that were not affected by the removal.
            int maxOriginalLcp = 0;
            // The max of LCPs to the left of the removed element's influence.
            if (i - 2 >= 0) {
                maxOriginalLcp = Math.max(maxOriginalLcp, prefixMax[i - 2]);
            }
            // The max of LCPs to the right of the removed element's influence.
            if (i + 1 < n - 1) {
                maxOriginalLcp = Math.max(maxOriginalLcp, suffixMax[i + 1]);
            }
            
            // Calculate the LCP of the new pair formed across the removal gap.
            int lcpNewPair = 0;
            if (i > 0 && i < n - 1) {
                lcpNewPair = calculateLcp(words[i - 1], words[i + 1]);
            }

            answer[i] = Math.max(maxOriginalLcp, lcpNewPair);
        }

        return answer;
    }

    /**
     * Helper function to calculate the length of the longest common prefix of two strings.
     */
    private int calculateLcp(String s1, String s2) {
        int minLen = Math.min(s1.length(), s2.length());
        int lcp = 0;
        for (int i = 0; i < minLen; i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                lcp++;
            } else {
                break;
            }
        }
        return lcp;
    }
}