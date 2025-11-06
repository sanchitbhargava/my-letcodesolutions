async function promiseAll(functions) {
    return new Promise((resolve, reject) => {
        let count = 0;
        const results = new Array(functions.length);

        for (let i = 0; i < functions.length; i++) {
            const fn = functions[i];
            fn()
                .then(res => {
                    results[i] = res;
                    count++;
                    if (count === functions.length) {
                        resolve(results);
                    }
                })
                .catch(err => reject(err));
        }
    });
}

/**
 * Example usage:
 * const promise = promiseAll([
 *   () => new Promise(res => res(42)),
 *   () => new Promise(res => setTimeout(() => res('done'), 500))
 * ]);
 *
 * promise.then(console.log); // [42, 'done']
 */
