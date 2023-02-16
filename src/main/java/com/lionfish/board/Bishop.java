package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.List;

public final class Bishop extends LongRangePiece {
    private static final List<Coords> dirs = Arrays.asList(
            new Coords(-1, -1),
            new Coords(-1, 1),
            new Coords(1, -1),
            new Coords(1, 1)
    );

    @Override
    protected final List<Coords> getDirections() { return dirs; }

    Bishop(Board board, Coords coords, PieceColor color) { super(board, coords, color); }

    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'B':'b'; }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Bw":"bb"; }
}
