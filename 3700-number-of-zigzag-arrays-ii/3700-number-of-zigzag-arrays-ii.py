class Solution:
    def zigZagArrays(self, n: int, l: int, r: int) -> int:
        MOD = 10**9 + 7
        k = r - l + 1
        
        # Edge case: If n is 1, any single element in the range is a valid zigzag array.
        if n == 1:
            return k % MOD
            
        # Helper function to multiply two k x k matrices
        def multiply(A, B):
            # Transpose B for highly optimized dot product using zip
            B_T = list(zip(*B))
            return [[sum(a * b for a, b in zip(row, col)) % MOD for col in B_T] for row in A]
            
        # Helper function for fast matrix exponentiation
        def power(A, p):
            # Identity matrix
            res = [[1 if i == j else 0 for j in range(k)] for i in range(k)]
            base = A
            
            while p > 0:
                if p % 2 == 1:
                    res = multiply(res, base)
                base = multiply(base, base)
                p //= 2
            return res
            
        # Build the transition matrix T
        T = [[0] * k for _ in range(k)]
        for u in range(1, k + 1):
            for w in range(k - u + 2, k + 1):
                T[u - 1][w - 1] = 1
                
        # Exponentiate the transition matrix by n - 1
        M = power(T, n - 1)
        
        # The total number of valid sequences starting with an "UP" move
        # is the sum of all elements in the resulting matrix M.
        total_sum = sum(sum(row) for row in M) % MOD
        
        # Multiply by 2 to account for sequences starting with a "DOWN" move
        return (2 * total_sum) % MOD