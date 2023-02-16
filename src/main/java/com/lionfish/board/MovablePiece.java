package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MovablePiece extends Piece {
    protected final PieceColor color;

    /**
     * Should create a list of possible moves,
     * this list may contain moves unavailable due to:
     * * potential discovered checks
     * * allied piece blockading the final square
     * @return potentially excessive list of available squares (see description)
     */
    protected abstract List<Coords> getAvailableSquaresPrimitive(Board board, Coords coords);

    MovablePiece(final PieceColor color) {
        this.color = color;
    }

    @Override
    public final List<Coords> getAvailableSquares(Board board, Coords coords) {
        /* Filter squares blockaded by allied piece and moves that allow check. */
        return this.getAvailableSquaresPrimitive(board, coords).stream()
                .filter(move -> board.getPieceAt(move).isCurrentlyHostile(board))
                .filter(move -> board.peekIsNotCheck(coords, move))
                .collect(Collectors.toList());
    }

    @Override
    public final boolean isEmpty() { return false; }
    @Override
    public final boolean isCurrentlyHostile(Board board) { return this.color != board.getCurrentColor(); }
    @Override
    public final boolean isCurrentlyCapturable(Board board) { return this.color != board.getCurrentColor(); }
}
