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

    Knight(PieceColor color) { super(color); }

    @Override
    protected List<Coords> getAvailableSquaresPrimitive(Board board, Coords coords) { return this.getAttackedSquares(board, coords); }

    @Override
    public List<Coords> getAttackedSquares(Board board, Coords coords) {
        return moves.stream()
                .map(coords::add)
                .filter(board::containsCoords)
                .collect(Collectors.toList());
    }
    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'N':'n'; }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Nw":"nb"; }
}
