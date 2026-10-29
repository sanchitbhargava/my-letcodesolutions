import java.util.*;

class Solution {
    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        int LOG = 1;
        while ((1 << LOG) <= n) LOG++;

        int[][] up = new int[LOG][n];
        int[] depth = new int[n];
        int[] tin = new int[n];
        int[] tout = new int[n];
        int[] parent = new int[n];

        Arrays.fill(parent, -1);

        // Iterative DFS: compute Euler tour intervals
        int[] stack = new int[n];
        int[] iter = new int[n];
        int top = 0, timer = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[top - 1];

            if (iter[u] == 0) {
                tin[u] = timer++;
            }

            if (iter[u] < adj[u].size()) {
                int v = adj[u].get(iter[u]++);

                if (v == parent[u]) continue;

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            } else {
                tout[u] = timer - 1;
                top--;
            }
        }

        for (int i = 0; i < n; i++) {
            up[0][i] = parent[i];
        }

        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                up[j][i] = up[j - 1][up[j - 1][i]];
            }
        }

        // Difference Fenwick tree: point update, prefix XOR
        int[] bit = new int[n + 2];

        class Fenwick {
            void update(int i, int val) {
                for (; i <= n + 1; i += i & -i) {
                    bit[i] ^= val;
                }
            }

            int query(int i) {
                int res = 0;
                for (; i > 0; i -= i & -i) {
                    res ^= bit[i];
                }
                return res;
            }

            void rangeUpdate(int l, int r, int val) {
                update(l + 1, val);
                update(r + 2, val);
            }
        }

        Fenwick fw = new Fenwick();
        int[] mask = new int[n];

        for (int u = 0; u < n; u++) {
            mask[u] = 1 << (s.charAt(u) - 'a');
            fw.rangeUpdate(tin[u], tout[u], mask[u]);
        }

        List<Boolean> ans = new ArrayList<>();

        for (String q : queries) {
            String[] p = q.split(" ");

            if (p[0].equals("update")) {
                int u = Integer.parseInt(p[1]);
                char c = p[2].charAt(0);
                int newMask = 1 << (c - 'a');

                if (mask[u] != newMask) {
                    int diff = mask[u] ^ newMask;
                    fw.rangeUpdate(tin[u], tout[u], diff);
                    mask[u] = newMask;
                }
            } else {
                int u = Integer.parseInt(p[1]);
                int v = Integer.parseInt(p[2]);
                int w = getLCA(u, v, depth, up, LOG);

                int pathMask = fw.query(tin[u] + 1)
                              ^ fw.query(tin[v] + 1)
                              ^ mask[w];

                ans.add((pathMask & (pathMask - 1)) == 0);
            }
        }

        return ans;
    }

    private int getLCA(int u, int v, int[] depth, int[][] up, int LOG) {
        if (depth[u] < depth[v]) {
            int t = u;
            u = v;
            v = t;
        }

        int diff = depth[u] - depth[v];

        for (int j = 0; j < LOG; j++) {
            if ((diff & (1 << j)) != 0) {
                u = up[j][u];
            }
        }

        if (u == v) return u;

        for (int j = LOG - 1; j >= 0; j--) {
            if (up[j][u] != up[j][v]) {
                u = up[j][u];
                v = up[j][v];
            }
        }

        return up[0][u];
    }
}