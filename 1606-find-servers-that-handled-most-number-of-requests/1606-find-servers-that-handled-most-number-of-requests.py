class Solution:
    def busiestServers(self, k: int, arrival: list[int], load: list[int]) -> list[int]:
        import heapq

        INF = 10**9

        # Segment tree stores the smallest available server ID
        # in each range. INF means no server is available.
        size = 1
        while size < k:
            size <<= 1

        tree = [INF] * (2 * size)

        # Initially every server is available
        for i in range(k):
            tree[size + i] = i

        for i in range(size - 1, 0, -1):
            tree[i] = min(tree[2 * i], tree[2 * i + 1])

        def update(pos, value):
            pos += size
            tree[pos] = value

            pos >>= 1
            while pos:
                tree[pos] = min(tree[2 * pos], tree[2 * pos + 1])
                pos >>= 1

        def query(left, right):
            # Minimum available server in [left, right]
            if left > right:
                return INF

            left += size
            right += size

            ans = INF

            while left <= right:
                if left & 1:
                    ans = min(ans, tree[left])
                    left += 1

                if not (right & 1):
                    ans = min(ans, tree[right])
                    right -= 1

                left >>= 1
                right >>= 1

            return ans

        # busy = (finish_time, server_id)
        busy = []

        handled = [0] * k

        for i in range(len(arrival)):
            time = arrival[i]

            # Release all servers whose requests have finished
            while busy and busy[0][0] <= time:
                finish, server = heapq.heappop(busy)

                # Make server available again
                update(server, server)

            if tree[1] == INF:
                # All servers are busy
                continue

            preferred = i % k

            # First try [preferred, k - 1]
            server = query(preferred, k - 1)

            # Wrap around if necessary
            if server == INF:
                server = query(0, preferred - 1)

            # Assign request
            update(server, INF)

            handled[server] += 1

            finish_time = time + load[i]

            heapq.heappush(
                busy,
                (finish_time, server)
            )

        maximum = max(handled)

        return [
            i for i in range(k)
            if handled[i] == maximum
        ]