from typing import List

class Solution:
    def sumCounts(self, nums: List[int]) -> int:
        MOD = 10**9 + 7
        n = len(nums)

        size = 4 * n
        total = [0] * size
        squares = [0] * size
        lazy = [0] * size

        def apply(node, l, r, val):
            length = r - l + 1
            squares[node] = (
                squares[node]
                + 2 * val * total[node]
                + length * val * val
            ) % MOD
            total[node] = (total[node] + length * val) % MOD
            lazy[node] = (lazy[node] + val) % MOD

        def push(node, l, r):
            if lazy[node] == 0 or l == r:
                return

            mid = (l + r) // 2
            val = lazy[node]

            apply(node * 2, l, mid, val)
            apply(node * 2 + 1, mid + 1, r, val)
            lazy[node] = 0

        def update(node, l, r, ql, qr):
            if ql > r or qr < l:
                return

            if ql <= l and r <= qr:
                apply(node, l, r, 1)
                return

            push(node, l, r)
            mid = (l + r) // 2

            update(node * 2, l, mid, ql, qr)
            update(node * 2 + 1, mid + 1, r, ql, qr)

            total[node] = (
                total[node * 2] + total[node * 2 + 1]
            ) % MOD
            squares[node] = (
                squares[node * 2] + squares[node * 2 + 1]
            ) % MOD

        last = {}
        ans = 0

        for i, x in enumerate(nums):
            prev = last.get(x, -1)
            update(1, 0, n - 1, prev + 1, i)
            ans = (ans + squares[1]) % MOD
            last[x] = i

        return ans