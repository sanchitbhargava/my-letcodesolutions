from typing import List

class Solution:
    def maximumScore(self, grid: List[List[int]]) -> int:
        n = len(grid)

        col_sum = [[0] * (n + 1) for _ in range(n)]
        for j in range(n):
            for i in range(n):
                col_sum[j][i+1] = col_sum[j][i] + grid[i][j]

        def S(j: int, a: int, b: int) -> int:
            if a >= b:
                return 0
            return col_sum[j][b] - col_sum[j][a]
        
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        for u in range(n + 1):
            for v in range(n + 1):
                dp[u][v] = S(0, u, v)

        for j in range(1, n):
            next_dp = [[0] * (n + 1) for _ in range(n + 1)]

            for u in range(n + 1):

                P = [0] * (n + 1)
                P[0] = dp[0][u]
                for t in range(1, n + 1):
                    P[t] = max(P[t-1], dp[t][u])

                Q = [0] * (n + 1)
                Q[n] = dp[n][u] + S(j, u, v)
                for t in range(n - 1, -1, -1):
                    Q[t] = max(Q[t+1], dp[t][u] + S(j, u, t))

                for v in range(n + 1):
                    val1 = Q[v]
                    val2 = (P[v-1] if v > 0 else float('-inf')) + S(j, u, v)

                    next_dp[u][v] = max(val1, val2) 
            
            dp = next_dp

        ans = 0
        for u in range(n + 1):
            if dp[u][0] > ans:
                ans = dp[u][0]
            
        return ans 