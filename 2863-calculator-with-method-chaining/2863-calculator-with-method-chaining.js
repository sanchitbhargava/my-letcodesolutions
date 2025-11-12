class Calculator {
    constructor(value) {
        this.x = value;
    }

    add(value) {
        this.x += value;
        return this;
    }

    subtract(value) {
        this.x -= value;
        return this;
    }

    multiply(value) {
        this.x *= value;
        return this;
    }

    divide(value) {
        if (value === 0) {
            throw new Error('Division by zero is not allowed');
        }
        this.x /= value;
        return this;
    }

    power(value) {
        this.x **= value;
        return this;
    }

    getResult() {
        return this.x;
    }
}

/**
 * Example usage:
 */
const calc = new Calculator(10);
console.log(
    calc.add(5).subtract(3).multiply(2).divide(4).power(2).getResult()
); // ((10 + 5 - 3) * 2 / 4) ^ 2 = 36
