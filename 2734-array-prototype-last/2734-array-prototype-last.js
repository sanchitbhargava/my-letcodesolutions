Array.prototype.last = function () {
    return this.length ? this.at(-1) : -1;
};

/**
 * Example usage:
 */
const arr = [1, 2, 3];
console.log(arr.last()); // 3
