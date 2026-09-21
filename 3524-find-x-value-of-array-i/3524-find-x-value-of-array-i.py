class Solution:
    def resultArray(self, nums: List[int], k: int) -> List[int]:
        result = [0] * k
        dp = [0] * k  
        
        for num in nums:
            m = num % k
            new_dp = [0] * k
            
           
            for r in range(k):
                if dp[r]:
                    new_dp[(r * m) % k] += dp[r]
            
            new_dp[m] += 1
            
            dp = new_dp
            
            for r in range(k):
                result[r] += dp[r]
        
        return result