class Solution:
    def validSequence(self, word1: str, word2: str) -> List[int]:
        n, m = len(word1), len(word2)

        right_match = [-1] * m
        i = n - 1
        for j in range(m - 1, -1, -1):
            while i >= 0 and word1[i] != word2[j]:
                i -= 1
            if i >= 0:
                right_match[j] = i
                i -= 1
            else:
                break

        seq = []
        changed = False
        i = 0

        for j in range(m):
            found = False
            while i < n:
                if word1[i] == word2[j]:

                    seq.append(i)
                    i += 1
                    found = True
                    break
                elif not changed:
                    if j + 1 == m or right_match[j + 1] > i:
                        seq.append(i)
                        changed = True
                        i += 1
                        found = True
                        break
                i += 1
                
            if not found:
                return []
        
        return seq
