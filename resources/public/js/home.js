let cellSize = 50;
let cellDensity = 25;
let pieceTypes = ["king", "queen", "rook", "bishop", "knight", "pawn"];
let nudge = { "king": 13, "queen": 13, "rook": 12, "bishop": 16, "knight": 12, "pawn": 14 };
let squash = { "king": 0, "queen": 0, "rook": 5, "bishop": 10, "knight": 11, "pawn": 15 };
var windowWidth = window.innerWidth;
var windowHeight = window.innerHeight;
var cellsWide = Math.floor(windowWidth / cellSize);
var cellsHigh = Math.floor(windowHeight / cellSize);

// Store active interval ID
let moveIntervalId;

class Piece {
    constructor() {
        this.x = -1; // Initialize to an invalid position
        this.y = -1; // Initialize to an invalid position
        this.type = pieceTypes[Math.floor(Math.random() * pieceTypes.length)];
        this.id = Math.floor(Math.random() * 100000);
        this.element = document.createElement("img");
        this.element.id = this.id;
        this.element.src = `/imgs/${this.type}.png`;
        this.element.style.position = "absolute";
        this.element.style.zIndex = -100;
        this.element.style.transition = "all 1s ease-in-out";
        document.body.appendChild(this.element);
    }
    move() {
        let moves = [];
        if (this.type === "king") {
            for (let dx = -1; dx <= 1; dx++) {
                for (let dy = -1; dy <= 1; dy++) {
                    if (dx !== 0 || dy !== 0) {
                        moves.push({ x: this.x + dx, y: this.y + dy });
                    }
                }
            }
        }
        if (this.type === "queen") {
            for (var dx = -4; dx <= 4; dx++) {
                for (var dy = -4; dy <= 4; dy++) {
                    if ((dx !== 0 || dy !== 0) && ((dx === 0) || (dy === 0) || (Math.abs(dx) === Math.abs(dy)))) {
                        moves.push({ x: this.x + dx, y: this.y + dy });
                    }
                }
            }
        }
        if (this.type === "rook") {
            for (var dx = -4; dx <= 4; dx++) {
                for (var dy = -4; dy <= 4; dy++) {
                    if ((dx !== 0 || dy !== 0) && (dx === 0 || dy === 0)) {
                        moves.push({ x: this.x + dx, y: this.y + dy });
                    }
                }
            }
        }
        if (this.type === "bishop") {
            for (var dx = -4; dx <= 4; dx++) {
                for (var dy = -4; dy <= 4; dy++) {
                    if ((dx !== 0 || dy !== 0) && Math.abs(dx) === Math.abs(dy)) {
                        moves.push({ x: this.x + dx, y: this.y + dy });
                    }
                }
            }
        }
        if (this.type === "knight") {
            const knightMoves = [
                { dx: -2, dy: -1 },
                { dx: -2, dy: 1 },
                { dx: -1, dy: -2 },
                { dx: -1, dy: 2 },
                { dx: 1, dy: -2 },
                { dx: 1, dy: 2 },
                { dx: 2, dy: -1 },
                { dx: 2, dy: 1 }
            ];

            for (const move of knightMoves) {
                const newX = this.x + move.dx;
                const newY = this.y + move.dy;
                moves.push({ x: newX, y: newY });
            }
        }
        if (this.type === "pawn") {
            const pawnMoves = [
                { dx: -1, dy: -1 },
                { dx: -1, dy: 0 },
                { dx: -1, dy: 1 },
                { dx: 0, dy: -1 },
                { dx: 0, dy: 1 },
                { dx: 1, dy: -1 },
                { dx: 1, dy: 0 },
                { dx: 1, dy: 1 },
                { dx: 0, dy: 2 },
                { dx: 0, dy: -2 },
                { dx: 2, dy: 0 },
                { dx: -2, dy: 0 }
            ];

            for (const move of pawnMoves) {
                const newX = this.x + move.dx;
                const newY = this.y + move.dy;
                moves.push({ x: newX, y: newY });
            }
        }

        // Filter out invalid moves (out of bounds) and occupied cells
        const validMoves = moves.filter(move =>
            move.x >= 0 && move.x < cellsWide &&
            move.y >= 0 && move.y < cellsHigh &&
            !pieces.some(p => p.x === move.x && p.y === move.y && p.id !== this.id) // Check for overlap
        );

        if (validMoves.length > 0) {
            let move = validMoves[Math.floor(Math.random() * validMoves.length)];
            this.x = move.x;
            this.y = move.y;
        }
    }
}

var pieces = [];
function genPieces() {
    pieces.forEach((piece) => { document.body.removeChild(piece.element) });
    pieces = [];
    let numCells = cellsWide * cellsHigh;
    let numPieces = Math.floor(numCells / cellDensity); // Use cellDensity for number of pieces

    for (var i = 0; i < numPieces; i++) {
        let newPiece = new Piece();
        let placed = false;
        // Try to find an empty spot for the new piece
        while (!placed) {
            let randX = Math.floor(Math.random() * cellsWide);
            let randY = Math.floor(Math.random() * cellsHigh);
            if (!pieces.some(p => p.x === randX && p.y === randY)) {
                newPiece.x = randX;
                newPiece.y = randY;
                placed = true;
            }
        }
        pieces.push(newPiece);
    }
}

function render() {
    for (var i = 0; i < pieces.length; i++) {
        let piece = pieces[i];
        piece.element.style.left = `${piece.x * cellSize + nudge[piece.type]}px`;
        piece.element.style.top = `${piece.y * cellSize + squash[piece.type]}px`;
        piece.element.style.height = `${cellSize - squash[piece.type]}px`;
    }
}

addEventListener("resize", () => {
    windowWidth = window.innerWidth;
    windowHeight = window.innerHeight;
    cellsWide = Math.floor(windowWidth / cellSize); // Recalculate cellsWide on resize
    cellsHigh = Math.floor(windowHeight / cellSize); // Recalculate cellsHigh on resize
    genPieces();
    render();
});

function moveAll() {
    pieces.forEach((piece) => {
        if (Math.random() < 0.67) {
            piece.move();
        }
    });
    render();
}

// Handle tab visibility to pause/resume movement
document.addEventListener("visibilitychange", () => {
    if (document.hidden) {
        clearInterval(moveIntervalId); // Pause when tab is hidden
    } else {
        moveIntervalId = setInterval(moveAll, 1000); // Resume when tab is visible
    }
});

// Initial generation and rendering
genPieces();
render();
// Start the interval when the page loads
moveIntervalId = setInterval(moveAll, 1000);
