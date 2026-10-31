from typing import List

class Solution:
    def uniquePaths(self, grid: List[List[int]]) -> int:
        MOD = 10**9 + 7
        m, n = len(grid), len(grid[0])

        # right[i][j]: destination when moving right from (i, j)
        # down[i][j]: destination when moving down from (i, j)
        right = [[None] * n for _ in range(m)]
        down = [[None] * n for _ in range(m)]

        # Compute right transitions in reverse row order
        for i in range(m - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                if j + 1 < n:
                    if grid[i][j + 1] == 0:
                        right[i][j] = (i, j + 1)
                    else:
                        right[i][j] = down[i][j + 1]

                if i + 1 < m:
                    if grid[i + 1][j] == 0:
                        down[i][j] = (i + 1, j)
                    else:
                        down[i][j] = right[i + 1][j]

        dp = [[0] * n for _ in range(m)]
        dp[0][0] = 1

        for i in range(m):
            for j in range(n):
                if dp[i][j] == 0:
                    continue

                if i == m - 1 and j == n - 1:
                    continue

                dest = right[i][j]
                if dest is not None:
                    x, y = dest
                    dp[x][y] = (dp[x][y] + dp[i][j]) % MOD

                dest = down[i][j]
                if dest is not None:
                    x, y = dest
                    dp[x][y] = (dp[x][y] + dp[i][j]) % MOD

        return dp[m - 1][n - 1]