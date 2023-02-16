package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.ArrayList;
import java.util.List;

public final class EmptyPiece extends Piece {
    EmptyPiece(Board board, Coords coords) { super(board, coords); }

    @Override
    public List<Coords> getAvailableSquares() { return new ArrayList<>(); }
    @Override
    public List<Coords> getAttackedSquares() { return new ArrayList<>(); }
    @Override
    public char getSymbol() { return 'x'; }

    @Override
    public boolean isEmpty() { return true; }
    @Override
    public boolean isCurrentlyHostile() { return true; }
    @Override
    public boolean isCurrentlyCapturable() { return false; }

    @Override
    public String getTextureName() { return "x"; }
}
