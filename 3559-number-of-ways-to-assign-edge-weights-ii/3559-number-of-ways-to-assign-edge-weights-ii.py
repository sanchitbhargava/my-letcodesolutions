from typing import List
 
class Solution:
    def assignEdgeWeights(self, edges: List[List[int]], queries: List[List[int]]) -> List[int]:
        MOD = 10**9 + 7

        n = len(edges) + 1

        graph = [[] for _ in range(n + 1)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)

        LOG = 17
        while(1 << LOG) <= n:
            Log += 1

        parent = [[0] * (n + 1) for _ in range(LOG)]
        depth = [0] * (n + 1)

        stack = [1]
        visited = [False] * (n + 1)
        visited[1] = True

        while stack:
            u = stack.pop()

            for v in graph[u]:
                if not visited[v]:
                    visited[v] = True
                    depth[v] = depth[u] + 1
                    parent[0][v] = u
                    stack.append(v)

        for k in range(1, LOG):
            for v in range(1, n + 1):
                parent[k][v] = parent[k - 1][parent[k -1][v]]

        def lca(u, v):
            if depth[u] < depth[v]:
                u, v = v, u

            diff = depth[u] - depth[v]

            bit = 0
            while diff:
                if diff & 1:
                    u = parent[bit][u]
                diff >>= 1
                bit += 1

            if u == v:
                return u
            
            for k in range(LOG - 1, -1, -1):
                if parent[k][u] != parent[k][v]:
                    u = parent[k][u]
                    v = parent[k][v]

            return parent[0][u]

        # max_depth = max(depth)
        # pow2 = [1] * (max_depth + 1)
        pow2 = [1] * n
        for i in range(1, n):
            pow2[i] = (pow2[i - 1] * 2) % MOD
        
        ans = []

        for u, v in queries:
            p = lca(u, v)

            length = depth[u] + depth[v] - 2 * depth[p]

            if length == 0:
                ans.append(0)
            else:
                ans.append(pow2[length - 1])

        return ans