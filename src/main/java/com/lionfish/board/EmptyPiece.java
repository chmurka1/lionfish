package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.ArrayList;
import java.util.List;

public final class EmptyPiece extends Piece {
    EmptyPiece() {}

    @Override
    public List<Coords> getAvailableSquares(Board board, Coords coords) { return new ArrayList<>(); }
    @Override
    public List<Coords> getAttackedSquares(Board board, Coords coords) { return new ArrayList<>(); }
    @Override
    public char getSymbol() { return '*'; }

    @Override
    public boolean isEmpty() { return true; }
    @Override
    public boolean isCurrentlyHostile(Board board) { return true; }
    @Override
    public boolean isCurrentlyCapturable(Board board) { return false; }

    @Override
    public String getTextureName() { return "x"; }
}
