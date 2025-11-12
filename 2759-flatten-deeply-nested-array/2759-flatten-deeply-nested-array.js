var flat = function (arr, n) {
    if (!n) {
        return arr;
    }
    const ans = [];
    for (const x of arr) {
        if (Array.isArray(x) && n) {
            ans.push(...flat(x, n - 1));
        } else {
            ans.push(x);
        }
    }
    return ans;
};

/**
 * Example usage:
 */
console.log(flat([1, [2, [3, [4]]], 5], 1)); // [1, 2, [3, [4]], 5]
console.log(flat([1, [2, [3, [4]]], 5], 2)); // [1, 2, 3, [4], 5]
console.log(flat([1, [2, [3, [4]]], 5], 3)); // [1, 2, 3, 4, 5]
