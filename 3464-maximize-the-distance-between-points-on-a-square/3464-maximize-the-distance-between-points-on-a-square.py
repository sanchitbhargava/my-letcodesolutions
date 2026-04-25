from typing import List

class Solution:
    def maxDistance(self, side: int, points: List[List[int]], k: int) -> int:
        A = []
        
        # Step 1: Map 2D boundary coordinates to 1D perimeter distance
        for x, y in points:
            if y == 0:
                A.append(x)
            elif x == side:
                A.append(side + y)
            elif y == side:
                A.append(3 * side - x)
            else:
                A.append(4 * side - y)
                
        # Sort the mapped perimeter points
        A.sort()
        
        n = len(A)
        L = 4 * side
        
        # Step 2: Unroll the circular boundary by duplicating the array
        B = A + [a + L for a in A]
        
        def check(D: int) -> bool:
            # Precompute the next valid point that is at least D distance away
            nxt = [2 * n] * (2 * n + 1)
            j = 0
            for i in range(2 * n):
                while j < 2 * n and B[j] - B[i] < D:
                    j += 1
                nxt[i] = j
            
            # Check if there is any valid sequence of k points
            for i in range(n):
                curr = i
                # Greedily jump to the next valid point k times
                for _ in range(k):
                    curr = nxt[curr]
                    # Early exit if we overshoot one full wrap-around
                    if curr > i + n:
                        break
                
                # If we successfully picked k points within one perimeter length
                if curr <= i + n:
                    return True
                    
            return False
            
        # Step 3: Binary Search on the Answer
        # The minimum possible distance is 1, and the maximum is L // k
        left = 1
        right = L // k
        ans = 1
        
        while left <= right:
            mid = (left + right) // 2
            if check(mid):
                ans = mid           # mid is achievable, try to maximize further
                left = mid + 1
            else:
                right = mid - 1     # mid is too large, reduce the distance
                
        return ans