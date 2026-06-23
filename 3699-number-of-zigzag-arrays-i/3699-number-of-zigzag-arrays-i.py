MOD = 10**9 + 7

class Solution:
    def zigZagArrays(self, n: int, l: int, r: int) -> int:
        m = r - l + 1

        dp_inc = [0] * m
        dp_dec = [0] * m

        # Length = 2
        for i in range(m):
            dp_inc[i] = i
            dp_dec[i] = m - 1 - i

        for _ in range(3, n + 1):
            new_inc = [0] * m
            new_dec = [0] * m

            prefix = 0
            for i in range(m):
                new_inc[i] = prefix
                prefix = (prefix + dp_dec[i]) % MOD

            suffix = 0
            for i in range(m - 1, -1, -1):
                new_dec[i] = suffix
                suffix = (suffix + dp_inc[i]) % MOD

            dp_inc = new_inc
            dp_dec = new_dec

        return (sum(dp_inc) + sum(dp_dec)) % MOD