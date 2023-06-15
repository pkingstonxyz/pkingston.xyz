let cellSize = 50;
let cellDensity = 25;
let pieceTypes = ["king", "queen", "rook", "bishop", "knight", "pawn"];
let nudge = {"king": 13, "queen": 13, "rook": 12, "bishop": 16, "knight": 12, "pawn": 14};
let squash = {"king": 0, "queen": 0, "rook": 5, "bishop": 10, "knight": 11, "pawn": 15};
var windowWidth = window.innerWidth;
var windowHeight = window.innerHeight;
var cellsWide = Math.floor(windowWidth / cellSize);
var cellsHigh = Math.floor(windowHeight / cellSize);

class Piece {
	constructor() {
		this.x = Math.floor(Math.random() * cellsWide);
		this.y = Math.floor(Math.random() * cellsHigh);
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
		while (true) {
			//console.log(this);
			//console.log(moves.length);
			let move = moves[Math.floor(Math.random() * moves.length)];
			if (move.x >= 0 && move.x < cellsWide && move.y >= 0 && move.y < cellsHigh) {
				this.x = move.x;
				this.y = move.y;

				//this.element.style.left = `${this.x * cellSize}px`;
				//this.element.style.top = `${this.y * cellSize}px`;
				break;
			}
		}
	}
}

var pieces = [];
function genPieces() {
	pieces.forEach((piece) => {document.body.removeChild(piece.element)});
	pieces = [];
	let numCells = cellsWide * cellsHigh;
	let numPieces = Math.floor(numCells / 25);
	for (var i = 0; i < numPieces; i++) {
		pieces.push(new Piece());
	}
}

function render() {
	for (var i = 0; i < pieces.length; i++) {
		let piece = pieces[i];
		piece.element.style.left = `${piece.x * cellSize + nudge[piece.type]}px`;
		piece.element.style.top = `${piece.y * cellSize + squash[piece.type]}px`;
		piece.element.style.height = `${cellSize - squash[piece.type]}px`;
		//
	}
}
addEventListener("resize", () => {
	windowWidth = window.innerWidth; 
	windowHeight = window.innerHeight;
	genPieces();
	render();
});

genPieces();
render();

function moveAll() {
	pieces.forEach((piece) => {
		if (Math.random() < 0.67){
		 piece.move();
		}
	});
	render();
}

setInterval(moveAll, 1000);
