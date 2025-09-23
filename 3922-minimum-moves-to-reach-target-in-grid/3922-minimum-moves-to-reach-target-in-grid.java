import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Solution {
    /**
     * A private helper class to use a coordinate pair as a key in the memoization map.
     */
    private static class Pair {
        long x, y;

        Pair(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private Map<Pair, Integer> memo;

    public int minMoves(int sx, int sy, int tx, int ty) {
        this.memo = new HashMap<>();
        int result = solve(sx, sy, tx, ty);
        
        // The helper returns a large value for impossible paths; convert it to -1.
        return result >= 1_000_000_000 ? -1 : result;
    }

    /**
     * Recursive helper function to find the minimum moves by working backward.
     */
    private int solve(long sx, long sy, long tx, long ty) {
        // Base Case 1: We've moved past the start; this path is invalid.
        if (sx > tx || sy > ty) {
            return 1_000_000_000; // Use a large number to represent infinity
        }
        // Base Case 2: We've reached the start coordinate.
        if (sx == tx && sy == ty) {
            return 0;
        }

        Pair currentTarget = new Pair(tx, ty);
        if (memo.containsKey(currentTarget)) {
            return memo.get(currentTarget);
        }

        int moves = 1_000_000_000;

        if (tx > ty) {
            if (ty == 0) { // Special case: target is on the x-axis
                // To reach (tx, 0), predecessor must be (tx/2, 0) via the (2*px, py) move.
                if (tx % 2 == 0) {
                    moves = 1 + solve(sx, sy, tx / 2, 0);
                }
            } else if (tx < 2 * ty) {
                // Predecessor px = tx - ty
                moves = 1 + solve(sx, sy, tx - ty, ty);
            } else { // tx >= 2 * ty
                // Predecessor px = tx / 2
                if (tx % 2 == 0) {
                    moves = 1 + solve(sx, sy, tx / 2, ty);
                }
                // If tx is odd, this state is unreachable from a valid predecessor.
            }
        } else if (ty > tx) { // Symmetric to the tx > ty case
            if (tx == 0) { // Special case: target is on the y-axis
                if (ty % 2 == 0) {
                    moves = 1 + solve(sx, sy, 0, ty / 2);
                }
            } else if (ty < 2 * tx) {
                moves = 1 + solve(sx, sy, tx, ty - tx);
            } else { // ty >= 2 * tx
                if (ty % 2 == 0) {
                    moves = 1 + solve(sx, sy, tx, ty / 2);
                }
            }
        } else { // tx == ty
            // A state (k, k) for k > 0 is only reachable from (k, 0) or (0, k).
            // We find the minimum moves to reach either of these predecessors, then add 1.
            if (tx > 0) {
                 moves = 1 + Math.min(solve(sx, sy, tx, 0), solve(sx, sy, 0, ty));
            }
        }

        memo.put(currentTarget, moves);
        return moves;
    }
}