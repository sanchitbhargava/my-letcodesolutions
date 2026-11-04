from typing import List
from heapq import heappush, heappop

class Solution:
    def minOperations(self, nums: List[int], x: int, k: int) -> int:
        n = len(nums)
        costs = [0] * (n - x + 1)

        # Two heaps to maintain the window median
        low = []   # Max heap (negative values)
        high = []  # Min heap
        delayed = {}
        low_size = 0
        high_size = 0
        low_sum = 0
        high_sum = 0

        def prune(heap, is_low):
            while heap:
                val = -heap[0] if is_low else heap[0]
                if delayed.get(val, 0) == 0:
                    break
                heappop(heap)
                delayed[val] -= 1
                if delayed[val] == 0:
                    del delayed[val]

        def rebalance():
            nonlocal low_size, high_size, low_sum, high_sum

            if low_size > high_size + 1:
                prune(low, True)
                v = -heappop(low)
                low_size -= 1
                low_sum -= v
                heappush(high, v)
                high_size += 1
                high_sum += v

            elif low_size < high_size:
                prune(high, False)
                v = heappop(high)
                high_size -= 1
                high_sum -= v
                heappush(low, -v)
                low_size += 1
                low_sum += v

        def add(v):
            nonlocal low_size, high_size, low_sum, high_sum

            prune(low, True)

            if not low or v <= -low[0]:
                heappush(low, -v)
                low_size += 1
                low_sum += v
            else:
                heappush(high, v)
                high_size += 1
                high_sum += v

            rebalance()

        def remove(v):
            nonlocal low_size, high_size, low_sum, high_sum

            prune(low, True)
            prune(high, False)

            delayed[v] = delayed.get(v, 0) + 1

            if v <= -low[0]:
                low_size -= 1
                low_sum -= v
                if v == -low[0]:
                    prune(low, True)
            else:
                high_size -= 1
                high_sum -= v
                if high and v == high[0]:
                    prune(high, False)

            rebalance()

        def get_cost():
            prune(low, True)
            median = -low[0]
            return (median * low_size - low_sum +
                    high_sum - median * high_size)

        # Calculate cost for the first window
        for i in range(x):
            add(nums[i])

        costs[0] = get_cost()

        # Calculate remaining window costs
        for i in range(x, n):
            remove(nums[i - x])
            add(nums[i])
            costs[i - x + 1] = get_cost()

        # DP for selecting k non-overlapping windows
        INF = float('inf')
        dp = [[INF] * (n + 1) for _ in range(k + 1)]
        dp[0] = [0] * (n + 1)

        for j in range(1, k + 1):
            for i in range(1, n + 1):
                dp[j][i] = dp[j][i - 1]

                if i >= x:
                    dp[j][i] = min(
                        dp[j][i],
                        dp[j - 1][i - x] + costs[i - x]
                    )

        return dp[k][n]