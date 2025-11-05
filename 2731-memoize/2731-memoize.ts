type Fn = (...params: any[]) => any;

function memoize(fn: Fn): Fn {
    const cache: Record<string, any> = {};

    return function (...args: any[]) {
        const key = JSON.stringify(args); // safely convert args array to a string key
        if (key in cache) {
            return cache[key];
        }
        const result = fn(...args);
        cache[key] = result;
        return result;
    };
}

/**
 * Example usage:
 * let callCount = 0;
 * const memoizedFn = memoize(function (a, b) {
 *   callCount += 1;
 *   return a + b;
 * });
 * 
 * console.log(memoizedFn(2, 3)); // 5
 * console.log(memoizedFn(2, 3)); // 5 (from cache)
 * console.log(callCount); // 1
 */
