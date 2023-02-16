package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.List;

public final class Queen extends LongRangePiece {
    private static final List<Coords> dirs = Arrays.asList(
            new Coords(0, 1),
            new Coords(0,-1),
            new Coords(1,0),
            new Coords(-1,0),
            new Coords(-1, -1),
            new Coords(-1, 1),
            new Coords(1, -1),
            new Coords(1, 1)
    );

    @Override
    protected List<Coords> getDirections() { return dirs; }

    Queen(PieceColor color) { super(color); }

    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'Q':'q'; }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Qw":"qb"; }
}