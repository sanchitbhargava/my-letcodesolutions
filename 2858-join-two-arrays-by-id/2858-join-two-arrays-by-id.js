function join(arr1, arr2) {
    const reducer = (acc, x) => {
        acc[x.id] = x;
        return acc;
    };

    const dict = arr1.reduce(reducer, {});

    arr2.forEach(x => {
        if (dict[x.id]) {
            Object.assign(dict[x.id], x);
        } else {
            dict[x.id] = x;
        }
    });

    return Object.values(dict);
}

const arr1 = [
  { id: 1, x: 1 },
  { id: 2, x: 9 }
];

const arr2 = [
  { id: 3, x: 5 },
  { id: 1, y: 2 }
];

console.log(join(arr1, arr2));
