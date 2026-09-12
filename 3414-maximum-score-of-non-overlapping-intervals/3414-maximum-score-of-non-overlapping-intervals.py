from typing import List
from bisect import bisect_right

class Solution:
    def maximumWeight(self, intervals: List[List[int]]) -> List[int]:
        n = len(intervals)

        # start, end, weight, original index
        a = [[l, r, w, i] for i, (l, r, w) in enumerate(intervals)]
        a.sort()

        starts = [x[0] for x in a]

        # dp[i][k] = best (score, indices) using i...n-1
        dp = [[(0, []) for _ in range(5)] for _ in range(n + 1)]

        def better(x, y):
            if x[0] != y[0]:
                return x if x[0] > y[0] else y

            # Same score -> lexicographically smaller indices
            return x if x[1] < y[1] else y

        for i in range(n - 1, -1, -1):
            for k in range(1, 5):

                # Option 1: skip current interval
                skip = dp[i + 1][k]

                # Option 2: take current interval
                # Next interval must start > current end
                nxt = bisect_right(starts, a[i][1])

                next_score, next_indices = dp[nxt][k - 1]

                take = (
                    a[i][2] + next_score,
                    sorted([a[i][3]] + next_indices)
                )

                dp[i][k] = better(take, skip)

        return dp[0][4][1]