package com.lionfish.board;

import com.lionfish.board.util.Coords;
import com.lionfish.board.util.PieceColor;

public class PieceFactory {
    private final Board board;
    public PieceFactory(Board board) {
        this.board = board;
    }

    public Piece getBySymbol(Coords coords, char symbol) {
        return switch (symbol) {
            case 'N' -> new Knight(this.board, coords, PieceColor.COLOR_WHITE);
            case 'n' -> new Knight(this.board, coords, PieceColor.COLOR_BLACK);
            case 'B' -> new Bishop(this.board, coords, PieceColor.COLOR_WHITE);
            case 'b' -> new Bishop(this.board, coords, PieceColor.COLOR_BLACK);
            case 'R' -> new Rook(this.board, coords, PieceColor.COLOR_WHITE);
            case 'r' -> new Rook(this.board, coords, PieceColor.COLOR_BLACK);
            case 'Q' -> new Queen(this.board, coords, PieceColor.COLOR_WHITE);
            case 'q' -> new Queen(this.board, coords, PieceColor.COLOR_BLACK);
            case 'K' -> new King(this.board, coords, PieceColor.COLOR_WHITE);
            case 'k' -> new King(this.board, coords, PieceColor.COLOR_BLACK);
            case 'P' -> new Pawn(this.board, coords, PieceColor.COLOR_WHITE);
            case 'p' -> new Pawn(this.board, coords, PieceColor.COLOR_BLACK);
            case '*' -> new EmptyPiece(this.board, coords);
            default -> throw new IllegalArgumentException("Piece symbol was not recognized");
        };
    }

    public Piece getBySymbol(int x, int y, char symbol) {
        Coords coords = new Coords(x, y);
        return this.getBySymbol(coords, symbol);
    }

    public Piece getNull(Coords coords) {
        return new EmptyPiece(this.board, coords);
    }
}
