class TimeLimitedCache {
    #cache = new Map();

    set(key, value, duration) {
        const isExist = this.#cache.has(key);

        // set or update the cache entry with new expiry
        this.#cache.set(key, [value, Date.now() + duration]);

        return isExist;
    }

    get(key) {
        if (this.#isExpired(key)) return -1;
        const res = this.#cache.get(key)?.[0] ?? -1;
        return res;
    }

    count() {
        const xs = Array.from(this.#cache).filter(([key]) => !this.#isExpired(key));
        return xs.length;
    }

    #isExpired = (key) =>
        !this.#cache.has(key) ||
        (this.#cache.get(key)[1] < Date.now());
}

/**
 * Example usage:
 */
const obj = new TimeLimitedCache();
console.log(obj.set(1, 42, 1000)); // false (new key)
console.log(obj.get(1)); // 42
console.log(obj.count()); // 1

setTimeout(() => {
    console.log(obj.get(1)); // -1 (expired)
    console.log(obj.count()); // 0
}, 1100);
