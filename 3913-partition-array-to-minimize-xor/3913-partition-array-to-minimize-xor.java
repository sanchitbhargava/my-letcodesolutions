import java.util.Arrays;

class Solution {
    /**
     * Partitions the array to minimize the maximum XOR sum of subarrays.
     *
     * @param nums The input integer array.
     * @param k    The number of non-empty subarrays to partition into.
     * @return The minimum possible value of the maximum XOR among the k subarrays.
     */
    public int minXor(int[] nums, int k) {
        int n = nums.length;

        // 1. Pre-compute prefix XORs for O(1) subarray XOR calculation.
        // prefixXor[i] stores the XOR sum of nums[0]...nums[i-1].
        int[] prefixXor = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixXor[i + 1] = prefixXor[i] ^ nums[i];
        }

        // 2. Initialize the DP table.
        // dp[p][i]: min-max XOR for partitioning the first 'i' elements into 'p' subarrays.
        int[][] dp = new int[k + 1][n + 1];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        // Base case: Partitioning 0 elements into 0 subarrays results in a max XOR of 0.
        dp[0][0] = 0;

        // 3. Fill the DP table using the recurrence relation.
        // p = number of partitions
        for (int p = 1; p <= k; p++) {
            // i = number of elements in the prefix
            // We need at least 'p' elements to form 'p' non-empty partitions.
            for (int i = p; i <= n; i++) {
                // j = split point. The last partition is nums[j...i-1].
                // The first p-1 partitions cover nums[0...j-1].
                for (int j = p - 1; j < i; j++) {
                    
                    // Only proceed if the state for p-1 partitions is reachable.
                    if (dp[p - 1][j] != Integer.MAX_VALUE) {
                        // XOR sum of the last subarray nums[j...i-1].
                        int lastSubarrayXor = prefixXor[i] ^ prefixXor[j];
                        
                        // The maximum XOR for this specific split configuration.
                        int maxForThisSplit = Math.max(dp[p - 1][j], lastSubarrayXor);
                        
                        // Update dp[p][i] with the minimum value found so far.
                        dp[p][i] = Math.min(dp[p][i], maxForThisSplit);
                    }
                }
            }
        }

        return dp[k][n];
    }
}