package com.lionfish.board;

import com.lionfish.board.util.PieceColor;

public class PieceFactory {
    public PieceFactory() {}

    public Piece getBySymbol(char symbol) {
        return switch (symbol) {
            case 'N' -> new Knight(PieceColor.COLOR_WHITE);
            case 'n' -> new Knight(PieceColor.COLOR_BLACK);
            case 'B' -> new Bishop(PieceColor.COLOR_WHITE);
            case 'b' -> new Bishop(PieceColor.COLOR_BLACK);
            case 'R' -> new Rook(PieceColor.COLOR_WHITE);
            case 'r' -> new Rook(PieceColor.COLOR_BLACK);
            case 'Q' -> new Queen(PieceColor.COLOR_WHITE);
            case 'q' -> new Queen(PieceColor.COLOR_BLACK);
            case 'K' -> new King(PieceColor.COLOR_WHITE);
            case 'k' -> new King(PieceColor.COLOR_BLACK);
            case 'P' -> new Pawn(PieceColor.COLOR_WHITE);
            case 'p' -> new Pawn(PieceColor.COLOR_BLACK);
            case '*' -> new EmptyPiece();
            default -> throw new IllegalArgumentException("Piece symbol was not recognized");
        };
    }

    public Piece getNull() {
        return new EmptyPiece();
    }
}
