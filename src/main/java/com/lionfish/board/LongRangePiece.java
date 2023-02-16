package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Template class for long-range pieces like queens, bishops, rooks, etc.
 */
public abstract class LongRangePiece extends MovablePiece {
    /**
     * @return the list of single steps towards every possible direction
     */
    protected abstract List<Coords> getDirections();

    @Override
    protected final List<Coords> getAvailableSquaresPrimitive(Board board, Coords coords) { return this.getAttackedSquares(board, coords); }

    LongRangePiece(PieceColor color) { super(color); }

    @Override
    public final List<Coords> getAttackedSquares(Board board, Coords coords) {
        ArrayList<Coords> res = new ArrayList<>();
        for(Coords dir : this.getDirections()) {
            Coords newCoords = coords;
            do {
                newCoords = newCoords.add(dir);
                if(!board.containsCoords(newCoords)) {
                    break;
                }
                res.add(newCoords);
            } while(board.getPieceAt(newCoords).isEmpty());
        }
        return res;
    }
}
