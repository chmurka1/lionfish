package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Knight extends MovablePiece {
    private static final List<Coords> moves = Arrays.asList(
            new Coords(-1, -2),
            new Coords(-1, 2),
            new Coords(1, -2),
            new Coords(1, 2),
            new Coords(-2, -1),
            new Coords(2, -1),
            new Coords(-2, 1),
            new Coords(2, 1)
    );

    Knight(Board board, Coords coords, PieceColor color) { super(board, coords, color); }

    @Override
    protected List<Coords> getAvailableSquaresPrimitive() { return this.getAttackedSquares(); }

    @Override
    public List<Coords> getAttackedSquares() {
        return moves.stream()
                .map(this.coords::add)
                .filter(this.getBoard()::containsCoords)
                .collect(Collectors.toList());
    }
    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'N':'n'; }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Nw":"nb"; }
}
