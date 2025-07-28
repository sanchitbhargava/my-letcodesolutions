class Solution {
    public int countMaxOrSubsets(int[] nums) {
        int maxOr= 0;
        for(int i: nums) maxOr|=i;
return solve(0,nums,0,maxOr);
    }

    private int solve(int i, int[] nums, int or, int maxOr) {
        if(i==nums.length){
            if(or==maxOr) return 1;
            return 0; 
        }
        int ans = 0;
        ans+=solve(i+1, nums, or|nums[i], maxOr);

        ans+=solve(i+1, nums, or, maxOr);
        return ans;

    }
}