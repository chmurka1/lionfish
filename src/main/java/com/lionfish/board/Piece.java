package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Collections;
import java.util.List;

public abstract class Piece {
    private final Board board;
    protected final Coords coords;

    Piece(Board board, Coords coords) {
        this.board = board;
        this.coords = coords;
    }

    /**
     * @return the complete list of squares to which the piece can move
     */
    public abstract List<Coords> getAvailableSquares();

    /**
     * @return the complete list of squares unavailable to the king due to the piece attacking it
     */
    public abstract List<Coords> getAttackedSquares();

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
    public abstract boolean isCurrentlyHostile();

    /**
     * @return true if the piece can be captured in the current turn, false otherwise,
     * empty pieces are not capturable, as they cannot be captured by pawn
     */
    public abstract boolean isCurrentlyCapturable();

    /**
     * @return the name of texture file
     */
    public abstract String getTextureName();

    /**
     * @return true if the piece can be promoted after reaching the last column, false otherwise
     */
    public boolean isPromotable() {
        return false;
    }

    /**
     * @return true if the piece can capture or be captured en passant, false otherwise
     */
    public boolean canBeCapturedEnPassant() {
        return false;
    }

    /**
     * @return true if the piece is a king
     */
    public boolean isKing() {
        return false;
    }

    /**
     * @return the board associated with the piece
     */
    public final Board getBoard() {
        return board;
    }

    /**
     * @return the coordinates of the piece
     */
    public final Coords getCoords() {
        return coords;
    }

    /**
     * @return list of coordinates of pieces captured by a move to dest
     */
    public List<Coords> getCaptureSquares(Coords dest) {
        return Collections.singletonList(dest);
    }

    /**
     * @return true if piece resets fifty move rule counter, false otherwise
     */
    public boolean isResettingFiftyRuleMove() {
        return false;
    }
}
