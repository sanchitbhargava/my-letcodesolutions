class Solution {
public:
    int maxDistinctElements(vector<int>& nums, int k) {
        int n = nums.size();
        sort(nums.begin(), nums.end());
        nums[0] -= k;
        int res = n;
        for(int i = 1; i < n; ++i){
            if(nums[i] + k < nums[i - 1] + 1){
                nums[i] = nums[i - 1];
                --res;
            }
            else{
                nums[i] = max(nums[i] - k, nums[i - 1] + 1);
            }
        }
        return res;
    }
};