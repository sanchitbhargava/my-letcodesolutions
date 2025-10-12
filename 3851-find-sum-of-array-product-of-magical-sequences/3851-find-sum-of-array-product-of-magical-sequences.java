import java.util.*;

class Solution {
    static final long MOD = 1_000_000_007L;

    public int magicalSum(int m, int k, int[] nums) {
        int n = nums.length;

        // Precompute factorials and inverse factorials up to m
        long[] fact = new long[m + 1];
        long[] invFact = new long[m + 1];
        fact[0] = 1;
        for (int i = 1; i <= m; i++) fact[i] = fact[i - 1] * i % MOD;
        invFact[m] = modPow(fact[m], MOD - 2);
        for (int i = m - 1; i >= 0; i--) invFact[i] = invFact[i + 1] * (i + 1) % MOD;

        // Precompute powNums[i][c] = nums[i]^c % MOD for c in [0..m]
        long[][] powNums = new long[n][m + 1];
        for (int i = 0; i < n; i++) {
            powNums[i][0] = 1;
            long base = nums[i] % MOD;
            for (int c = 1; c <= m; c++) powNums[i][c] = powNums[i][c - 1] * base % MOD;
        }

        // posMax: process up to n-1 positions (indices), plus extra positions to flush carries.
        // Worst-case carry might propagate up to about log2(m) bits. m <= 30 -> safe extra 31.
        int posMax = n + 31; // safe upper bound

        // DP arrays: current and next.
        // Dimensions: carry in [0..m], used in [0..m], setbits in [0..m]
        int C = m + 1;
        int U = m + 1;
        int S = m + 1;
        long[][][] dpCur = new long[C][U][S];
        long[][][] dpNext = new long[C][U][S];

        // initial state: pos = 0, carry = 0, used = 0, setbits = 0 -> value = 1
        dpCur[0][0][0] = 1L;

        for (int pos = 0; pos < posMax; pos++) {
            // zero dpNext
            for (int carry = 0; carry <= m; carry++) {
                for (int used = 0; used <= m; used++) {
                    Arrays.fill(dpNext[carry][used], 0L);
                }
            }

            boolean hasIndex = (pos < n);

            for (int carry = 0; carry <= m; carry++) {
                for (int used = 0; used <= m; used++) {
                    for (int setbits = 0; setbits <= m; setbits++) {
                        long val = dpCur[carry][used][setbits];
                        if (val == 0) continue;

                        if (hasIndex) {
                            // choose c occurrences for index = pos
                            int maxC = m - used;
                            for (int c = 0; c <= maxC; c++) {
                                int total = c + carry;
                                int bit = total & 1;
                                int newCarry = total >>> 1;
                                int newUsed = used + c;
                                int newSet = setbits + bit;
                                if (newSet > m) continue; // impossible but safe

                                // multiply val by nums[pos]^c * invFact[c]
                                long mul = powNums[pos][c] * invFact[c] % MOD;
                                long add = val * mul % MOD;
                                dpNext[newCarry][newUsed][newSet] += add;
                                if (dpNext[newCarry][newUsed][newSet] >= MOD) dpNext[newCarry][newUsed][newSet] -= MOD;
                            }
                        } else {
                            // no more indices: c must be 0; only propagate carry bit
                            int total = carry; // c = 0
                            int bit = total & 1;
                            int newCarry = total >>> 1;
                            int newUsed = used; // unchanged
                            int newSet = setbits + bit;
                            if (newSet <= m) {
                                dpNext[newCarry][newUsed][newSet] += val;
                                if (dpNext[newCarry][newUsed][newSet] >= MOD) dpNext[newCarry][newUsed][newSet] -= MOD;
                            }
                        }
                    }
                }
            }

            // swap dpCur and dpNext
            long[][][] tmp = dpCur;
            dpCur = dpNext;
            dpNext = tmp;
        }

        // After processing all positions, valid states: carry == 0, used == m, setbits == k
        long res = dpCur[0][m][k];
        // multiply by fact[m] to account for permutations (multinomial numerator)
        res = res * fact[m] % MOD;
        return (int) res;
    }

    private long modPow(long a, long e) {
        long res = 1 % MOD;
        a %= MOD;
        while (e > 0) {
            if ((e & 1L) == 1L) res = res * a % MOD;
            a = a * a % MOD;
            e >>= 1L;
        }
        return res;
    }
}
