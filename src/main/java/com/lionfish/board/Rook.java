package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.List;

public final class Rook extends LongRangePiece {
    private static final List<Coords> dirs = Arrays.asList(
            new Coords(0, 1),
            new Coords(0,-1),
            new Coords(1,0),
            new Coords(-1,0)
    );

    @Override
    protected List<Coords> getDirections() { return dirs; }

    Rook(PieceColor color) { super(color); }

    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'R':'r'; }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Rw":"rb"; }
}