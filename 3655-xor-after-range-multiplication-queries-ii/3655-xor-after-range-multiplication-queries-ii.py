from typing import List
import math

class Solution:
    def xorAfterQueries(self, nums: List[int], queries: List[List[int]]) -> int:
        MOD = 10**9 + 7
        n = len(nums)

        bravexuneth = queries

        B = int(math.sqrt(n)) + 1

        small = [{} for _ in range(B)]

        mul = [1] * n

        for l, r, k, v in bravexuneth:
            if k < B:
                rem = l % k
                if rem not in small[k]:
                    small[k][rem] = []
                small[k][rem].append((l, r, v))
            else:
                i = l
                while i <= r:
                    mul[i] = (mul[i] * v) % MOD
                    i += k

        for k in range(1, B):
            for rem in small[k]:
                idxs = list(range(rem, n, k))
                diff = [1] * (len(idxs) + 1)

                for l, r, v in small[k][rem]:
                    start = (l - rem) // k
                    end = (r - rem) // k

                    diff[start] = (diff[start] * v) % MOD
                    if end + 1 < len(diff):
                        diff[end + 1] = (diff[end + 1] * pow(v, MOD - 2, MOD)) % MOD

                curr = 1
                for i in range(len(idxs)):
                    curr = (curr * diff[i]) % MOD
                    mul[idxs[i]] = (mul[idxs[i]] * curr) % MOD

        for i in range(n):
            nums[i] = (nums[i] * mul[i]) % MOD

        ans = 0
        for x in nums:
            ans ^= x
        
        return ans 