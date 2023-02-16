package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class King extends MovablePiece {
    private static final List<Coords> moves = Arrays.asList(
            new Coords(0, 1),
            new Coords(0,-1),
            new Coords(1,0),
            new Coords(-1,0),
            new Coords(-1, -1),
            new Coords(-1, 1),
            new Coords(1, -1),
            new Coords(1, 1)
    );

    private static final List<Coords> castling_moves = Arrays.asList(
            new Coords(2,0),
            new Coords(-2,0)
    );

    King(Board board, Coords coords, PieceColor color) { super(board, coords, color); }

    protected List<Coords> getCastlingSquares() {
        return castling_moves.stream()
                .map(this.coords::add)
                .filter(this.getBoard()::containsCoords)
                .filter(move->this.getBoard().isCastlingPossible(coords,move))
                .collect(Collectors.toList());
    }

    @Override
    protected List<Coords> getAvailableSquaresPrimitive() {
        return Stream.concat(
                    this.getAttackedSquares().stream(),
                    this.getCastlingSquares().stream()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Coords> getAttackedSquares() {
        return moves.stream()
                .map(this.coords::add)
                .filter(this.getBoard()::containsCoords)
                .collect(Collectors.toList());
    }
    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'K':'k'; }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Kw":"kb"; }
}
