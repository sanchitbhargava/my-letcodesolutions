from typing import List
from bisect import bisect_left, bisect_right, insort

class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.seg = [0] * (4 * n)

    def update(self, idx, val, node, l, r):
        if l == r:
            self.seg[node] = val
            return

        mid = (l + r) // 2

        if idx <= mid:
            self.update(idx, val, 2 * node, l, mid)
        else:
            self.update(idx, val, 2 * node + 1, mid + 1, r)

        self.seg[node] = max(self.seg[2 * node],
                             self.seg[2 * node + 1])

    def query(self, ql, qr, node, l, r):
        if qr < l or r < ql:
            return 0

        if ql <= l and r <= qr:
            return self.seg[node]

        mid = (l + r) // 2

        return max(
            self.query(ql, qr, 2 * node, l, mid),
            self.query(ql, qr, 2 * node + 1, mid + 1, r)
        )


class Solution:
    def getResults(self, queries: List[List[int]]) -> List[bool]:

        max_x = max(q[1] for q in queries) + 1

        obstacles = [0]
        seg = SegmentTree(max_x + 1)

        ans = []

        for q in queries:

            if q[0] == 1:
                x = q[1]

                pos = bisect_left(obstacles, x)

                prev = obstacles[pos - 1]
                nxt = obstacles[pos] if pos < len(obstacles) else max_x

                insort(obstacles, x)

                seg.update(x, x - prev, 1, 0, max_x)

                if nxt != max_x:
                    seg.update(nxt, nxt - x, 1, 0, max_x)

            else:
                x, sz = q[1], q[2]

                pos = bisect_right(obstacles, x)

                best = seg.query(0, x, 1, 0, max_x)

                prev = obstacles[pos - 1]

                best = max(best, x - prev)

                ans.append(best >= sz)

        return ans