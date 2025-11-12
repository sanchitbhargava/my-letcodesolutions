class EventEmitter {
    constructor() {
        this.d = new Map();
    }

    subscribe(eventName, callback) {
        this.d.set(eventName, (this.d.get(eventName) || new Set()).add(callback));
        return {
            unsubscribe: () => {
                this.d.get(eventName)?.delete(callback);
            },
        };
    }

    emit(eventName, args = []) {
        const callbacks = this.d.get(eventName);
        if (!callbacks) return [];
        return [...callbacks].map(callback => callback(...args));
    }
}

/**
 * Example usage:
 */
const emitter = new EventEmitter();

function onClickCallback() {
    return 99;
}

const sub = emitter.subscribe('onClick', onClickCallback);

console.log(emitter.emit('onClick')); // [99]
sub.unsubscribe();
console.log(emitter.emit('onClick')); // []
