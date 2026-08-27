class Solution:
    def lexGreaterPermutation(self, s: str, target: str) -> str:
        n = len(s)

        # Count characters in s
        freq = [0] * 26
        for ch in s:
            freq[ord(ch) - ord('a')] += 1

        # Store how many characters of target we matched
        matched = 0

        # Match target from left to right
        while matched < n:
            idx = ord(target[matched]) - ord('a')

            if freq[idx] == 0:
                break

            freq[idx] -= 1
            matched += 1

        # If target was completely matched, we need
        # to backtrack because target itself is not valid.
        if matched == n:
            matched = n - 1

            # Restore the last matched character
            idx = ord(target[matched]) - ord('a')
            freq[idx] += 1

        # Try to make the string greater at the latest
        # possible position.
        for i in range(matched, -1, -1):

            target_idx = ord(target[i]) - ord('a')

            # If i is before the point where matching failed,
            # restore target[i] because we are going to change it.
            if i < matched:
                freq[ord(target[i]) - ord('a')] += 1

            # Find the smallest character greater than target[i]
            for c in range(target_idx + 1, 26):
                if freq[c] > 0:

                    freq[c] -= 1

                    # Build the smallest possible suffix
                    suffix = []

                    for x in range(26):
                        while freq[x] > 0:
                            suffix.append(chr(x + ord('a')))
                            freq[x] -= 1

                    return target[:i] + chr(c + ord('a')) + ''.join(suffix)

        return ""