package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Collections;
import java.util.List;

public abstract class Piece {

    Piece() {}

    /**
     * @return the complete list of squares to which the piece can move
     */
    public abstract List<Coords> getAvailableSquares(Board board, Coords coords);

    /**
     * @return the complete list of squares unavailable to the king due to the piece attacking it
     */
    public abstract List<Coords> getAttackedSquares(Board board, Coords coords);

    /**
     * @return the symbol to be used in algebraic notation
     */
    public abstract char getSymbol();

    /**
     * @return true if the object represents an empty square, false otherwise
     */
    public abstract boolean isEmpty();

    /**
     * @return true if the piece cannot move in the current turn, false otherwise
     * empty pieces are considered hostile
     */
    public abstract boolean isCurrentlyHostile(Board board);

    /**
     * @return true if the piece can be captured in the current turn, false otherwise,
     * empty pieces are not capturable, as they cannot be captured by pawn
     */
    public abstract boolean isCurrentlyCapturable(Board board);

    /**
     * @return the name of texture file
     */
    public abstract String getTextureName();

    /**
     * @return true if the piece can be promoted after reaching the last column, false otherwise
     */
    public boolean isPromotable(Board board, Coords coords) {
        return false;
    }

    /**
     * @return true if the piece can capture or be captured en passant, false otherwise
     */
    public boolean canBeCapturedEnPassant(Board board, Coords coords) {
        return false;
    }

    /**
     * @return true if the piece is a king
     */
    public boolean isKing() {
        return false;
    }

    /**
     * @return list of coordinates of pieces captured by a move to dest
     */
    public List<Coords> getCaptureSquares(Board board, Coords targ, Coords dest) {
        return Collections.singletonList(dest);
    }

    /**
     * @return true if piece resets fifty move rule counter, false otherwise
     */
    public boolean isResettingFiftyRuleMove() {
        return false;
    }
}
