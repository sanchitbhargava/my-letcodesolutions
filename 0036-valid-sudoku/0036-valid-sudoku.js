/**
 * @param {character[][]} board
 * @return {boolean}
 */
var isValidSudoku = function(board) {
    // Initialize arrays of sets to track seen numbers for each row, column, and 3x3 box.
    const rows = Array(9).fill(0).map(() => new Set());
    const cols = Array(9).fill(0).map(() => new Set());
    const boxes = Array(9).fill(0).map(() => new Set());
    
    // Iterate through each cell of the 9x9 board.
    for (let r = 0; r < 9; r++) {
        for (let c = 0; c < 9; c++) {
            const num = board[r][c];
            
            // If the cell is empty, skip it.
            if (num === '.') {
                continue;
            }
            
            // Calculate the index for the 3x3 box.
            // This formula maps the (r, c) coordinate to a unique box index from 0 to 8.
            const boxIndex = Math.floor(r / 3) * 3 + Math.floor(c / 3);
            
            // Check if the number has already been seen in the current row, column, or box.
            if (rows[r].has(num) || cols[c].has(num) || boxes[boxIndex].has(num)) {
                return false; // Found a duplicate, so the board is invalid.
            }
            
            // If no duplicate is found, add the number to all three sets.
            rows[r].add(num);
            cols[c].add(num);
            boxes[boxIndex].add(num);
        }
    }
    
    // If we get through the entire board without finding any duplicates, the board is valid.
    return true;
};