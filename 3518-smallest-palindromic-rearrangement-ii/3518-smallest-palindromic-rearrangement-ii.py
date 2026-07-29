class Solution:
    def smallestPalindrome(self, s: str, k: int) -> str:
        n = len(s)
        freq = [0] * 26

        for ch in s:
            freq[ord(ch) - ord('a')] += 1

        half = [x // 2 for x in freq]
        half_len = n // 2

        def count_permutations(cnt, length):
            res = 1
            remaining = length

            for x in cnt:
                if x == 0:
                    continue
                
                choose = 1

                for j in range(1, x + 1):
                    choose = choose * (remaining - x + j) // j

                    if choose >= k:
                        choose = k
                        break

                res *= choose

                if res >= k:
                    return k
                
                remaining -= x

            return res
        
        left = []

        for pos in range(half_len):

            found = False

            for c in range(26):
                if half[c] == 0:
                    continue

                half[c] -= 1

                ways = count_permutations(
                    half,
                    half_len - pos - 1
                )

                if k > ways:
                    k -= ways
                    half[c] += 1
                else:
                    left.append(chr(ord('a') + c))
                    found = True
                    break

            if not found:
                return ""

        left = ''.join(left)

        middle = ""

        if n % 2 == 1:
            for c in range(26):
                if freq[c] % 2 == 1:
                    middle = chr(ord('a') + c)
                    break
        
        return left + middle + left[::-1]
        