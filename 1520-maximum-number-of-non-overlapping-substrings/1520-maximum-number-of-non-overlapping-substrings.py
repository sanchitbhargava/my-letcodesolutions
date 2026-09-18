class Solution:
    def maxNumOfSubstrings(self, s: str) -> list[str]:
        first, last = {}, {}
        for i, ch in enumerate(s):
            if ch not in first:
                first[ch] = i
            last[ch] = i

        res = []
        prev_end = -1

        for i, ch in enumerate(s):
            if i != first[ch]:
                continue

            right = last[ch]
            j = i
            ok = True
            while j <= right:
                c = s[j]
                if first[c] < i:
                    ok = False
                    break
                if last[c] > right:
                    right = last[c]
                j += 1

            if not ok:
                continue
            
            if i > prev_end:
                res.append(s[i:right + 1])
            else:
                res[-1] = s[i:right + 1]
            prev_end = right
            
        return res
