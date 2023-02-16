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
    protected final List<Coords> getAvailableSquaresPrimitive() { return this.getAttackedSquares(); }

    LongRangePiece(Board board, Coords coords, PieceColor color) { super(board, coords, color); }

    @Override
    public final List<Coords> getAttackedSquares() {
        ArrayList<Coords> res = new ArrayList<>();
        for(Coords dir : this.getDirections()) {
            Coords newCoords = this.coords;
            do {
                newCoords = newCoords.add(dir);
                if(!this.getBoard().containsCoords(newCoords)) {
                    break;
                }
                res.add(newCoords);
            } while(this.getBoard().getPieceAt(newCoords).isEmpty());
        }
        return res;
    }
}
