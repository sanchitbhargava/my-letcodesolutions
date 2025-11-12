class ArrayWrapper {
    constructor(nums) {
        this.nums = nums;
        this.s = nums.reduce((a, b) => a + b, 0);
    }

    valueOf() {
        return this.s;
    }

    toString() {
        return `[${this.nums}]`;
    }
}

/**
 * Example usage:
 */
const obj1 = new ArrayWrapper([1, 2]);
const obj2 = new ArrayWrapper([3, 4]);

console.log(obj1 + obj2);     // 10
console.log(String(obj1));    // "[1,2]"
console.log(String(obj2));    // "[3,4]"
