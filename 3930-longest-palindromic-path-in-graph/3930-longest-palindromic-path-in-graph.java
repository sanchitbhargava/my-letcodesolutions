import java.util.*;

class Solution {
    /**
     * Calculates the length of the longest simple path in a graph
     * whose node labels form a palindrome.
     *
     * @param n     The number of nodes in the graph.
     * @param edges An array of edges connecting the nodes.
     * @param label A string where label.charAt(i) is the character for node i.
     * @return The maximum length of a palindromic path.
     */
    public int maxLen(int n, int[][] edges, String label) {
        // Base cases for small n.
        if (n <= 1) {
            return n;
        }

        // 1. Build an adjacency list for the graph.
        List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }

        // 2. Initialize data structures for BFS.
        // The state is an array {u, v, mask}, where u and v are the path endpoints,
        // and mask is a bitmask of the nodes in the path.
        Queue<int[]> queue = new LinkedList<>();
        // pathLength[u][v][mask] stores the length of the path for a given state.
        // It's also used to track visited states (a value of 0 means not visited).
        int[][][] pathLength = new int[n][n][1 << n];
        int maxLen = 1;

        // 3. Seed the BFS with all paths of length 1.
        for (int i = 0; i < n; i++) {
            int mask = 1 << i;
            pathLength[i][i][mask] = 1;
            queue.offer(new int[]{i, i, mask});
        }

        // 4. Seed the BFS with all paths of length 2.
        for (int u = 0; u < n; u++) {
            for (int v : adj[u]) {
                // Process each edge only once to avoid redundancy.
                if (u >= v) continue;
                
                if (label.charAt(u) == label.charAt(v)) {
                    int mask = (1 << u) | (1 << v);
                    pathLength[u][v][mask] = 2;
                    pathLength[v][u][mask] = 2; // Add symmetric state
                    queue.offer(new int[]{u, v, mask});
                    queue.offer(new int[]{v, u, mask});
                    maxLen = 2;
                }
            }
        }

        // 5. Run the BFS to find longer palindromic paths.
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int u = state[0];
            int v = state[1];
            int mask = state[2];
            int currentLen = pathLength[u][v][mask];

            // Try to extend the path from endpoint u.
            for (int uPrev : adj[u]) {
                // Skip if the new node is already in the path.
                if (((mask >> uPrev) & 1) == 1) {
                    continue;
                }

                // Try to extend the path from endpoint v.
                for (int vNext : adj[v]) {
                    // Skip if the new node is already in the path or is the same as uPrev.
                    if (((mask >> vNext) & 1) == 1 || uPrev == vNext) {
                        continue;
                    }

                    // Check if the new endpoints form a palindrome.
                    if (label.charAt(uPrev) == label.charAt(vNext)) {
                        int newMask = mask | (1 << uPrev) | (1 << vNext);
                        
                        // If we haven't visited this new state yet, update and enqueue it.
                        if (pathLength[uPrev][vNext][newMask] == 0) {
                            int newLen = currentLen + 2;
                            pathLength[uPrev][vNext][newMask] = newLen;
                            pathLength[vNext][uPrev][newMask] = newLen; // Symmetric
                            
                            queue.offer(new int[]{uPrev, vNext, newMask});
                            queue.offer(new int[]{vNext, uPrev, newMask});
                            
                            maxLen = Math.max(maxLen, newLen);
                        }
                    }
                }
            }
        }

        return maxLen;
    }
}