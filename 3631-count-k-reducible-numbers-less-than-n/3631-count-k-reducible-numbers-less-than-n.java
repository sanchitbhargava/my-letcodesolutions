import java.util.HashSet;
import java.util.Set;

class Solution {
    private static final int MOD = 1_000_000_007;
    private long[][] combinations;

    public int countKReducibleNumbers(String s, int k) {
        int len = s.length();

        // 1. Pre-compute combinations C(i, j) % MOD.
        precomputeCombinations(len);

        // 2. Determine the set of "good" popcounts.
        // A number x > 1 is k-reducible iff popcount(x) is (k-1)-reducible.
        // First, we find all numbers y <= len that are (k-1)-reducible.
        
        // isRed[y][kv] is true if the number y is kv-reducible.
        boolean[][] isRed = new boolean[len + 1][k];
        if (k > 0) {
            // Base case: for k=0, only 1 is reducible.
            isRed[1][0] = true;
            
            // Iteratively build up the table up to k-1 reducibility.
            for (int kv = 1; kv < k; kv++) {
                for (int y = 1; y <= len; y++) {
                    int pc = Integer.bitCount(y);
                    // y is kv-reducible if it was already (kv-1)-reducible,
                    // or if its popcount was (kv-1)-reducible.
                    if (isRed[y][kv - 1] || (pc <= len && isRed[pc][kv - 1])) {
                        isRed[y][kv] = true;
                    }
                }
            }
        }
        
        // The set of "good" popcounts are the numbers that are (k-1)-reducible.
        Set<Integer> goodPopcounts = new HashSet<>();
        if (k > 0) {
            for (int i = 1; i <= len; i++) {
                if (isRed[i][k - 1]) {
                    goodPopcounts.add(i);
                }
            }
        }

        // 3. Count numbers < n using the combinatorics-based digit DP.
        long ans = 0;
        int onesSoFar = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == '1') {
                // We are at index i. If we place a '0' here, the resulting number
                // will be smaller than n. The remaining bits can be anything.
                // We have 'onesSoFar' set bits in the prefix.
                int remainingLen = len - 1 - i;
                
                // We need to choose 'c' more ones from the remaining bits.
                for (int c = 0; c <= remainingLen; c++) {
                    if (goodPopcounts.contains(onesSoFar + c)) {
                        ans = (ans + combinations[remainingLen][c]) % MOD;
                    }
                }
                // We now fix the current bit as '1' to continue matching the prefix of n.
                onesSoFar++;
            }
        }

        // The loop correctly counts all positive integers strictly less than n
        // that have a "good" popcount. We don't need to consider n itself.
        return (int) ans;
    }

    /**
     * Pre-computes binomial coefficients C(n, k) using Pascal's triangle.
     */
    private void precomputeCombinations(int n) {
        combinations = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            combinations[i][0] = 1; // C(i, 0) = 1
            for (int j = 1; j <= i; j++) {
                combinations[i][j] = (combinations[i - 1][j - 1] + combinations[i - 1][j]) % MOD;
            }
        }
    }
}