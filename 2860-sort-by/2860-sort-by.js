function sortBy(arr, fn) {
    return arr.sort((a, b) => fn(a) - fn(b));
}

/**
 * Example usages:
 */


console.log(sortBy([5, 2, 3, 1], x => x)); 

console.log(sortBy([{x: 2}, {x: 3}, {x: 1}], item => item.x)); 

console.log(sortBy(['apple', 'kiwi', 'banana'], s => s.length)); 
