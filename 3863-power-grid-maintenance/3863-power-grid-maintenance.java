import java.util.*;

class Solution {
    int[] parent;
    int[] rank;

    // DSU: Find with path compression
    private int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    // DSU: Union by rank
    private void union(int x, int y) {
        int px = find(x);
        int py = find(y);
        if (px == py) return;
        if (rank[px] < rank[py]) {
            parent[px] = py;
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
        } else {
            parent[py] = px;
            rank[px]++;
        }
    }

    public int[] processQueries(int c, int[][] connections, int[][] queries) {
        parent = new int[c + 1];
        rank = new int[c + 1];
        boolean[] online = new boolean[c + 1];
        Arrays.fill(online, true);

        // Initialize DSU
        for (int i = 1; i <= c; i++) {
            parent[i] = i;
        }

        // Union all connections
        for (int[] conn : connections) {
            union(conn[0], conn[1]);
        }

        // Map each component (root) to its online station TreeSet
        Map<Integer, TreeSet<Integer>> gridOnlineMap = new HashMap<>();
        for (int i = 1; i <= c; i++) {
            int root = find(i);
            gridOnlineMap.computeIfAbsent(root, k -> new TreeSet<>()).add(i);
        }

        List<Integer> result = new ArrayList<>();

        for (int[] q : queries) {
            int type = q[0], x = q[1];
            int root = find(x);

            if (type == 1) { // Maintenance check
                if (online[x]) {
                    result.add(x);
                } else {
                    TreeSet<Integer> set = gridOnlineMap.get(root);
                    if (set == null || set.isEmpty()) {
                        result.add(-1);
                    } else {
                        result.add(set.first());
                    }
                }
            } else if (type == 2) { // Go offline
                if (online[x]) {
                    online[x] = false;
                    TreeSet<Integer> set = gridOnlineMap.get(root);
                    if (set != null) set.remove(x);
                }
            }
        }

        // Convert result list to array
        int[] ans = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ans[i] = result.get(i);
        }
        return ans;
    }
}
