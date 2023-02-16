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
    protected abstract List<Coords> getAvailableSquaresPrimitive();

    MovablePiece(final Board board, final Coords coords, final PieceColor color) {
        super(board, coords);
        this.color = color;
    }

    @Override
    public final List<Coords> getAvailableSquares() {
        /* Filter squares blockaded by allied piece and moves that allow check. */
        return this.getAvailableSquaresPrimitive().stream()
                .filter(move -> this.getBoard().getPieceAt(move).isCurrentlyHostile())
                .filter(move -> this.getBoard().peekIsNotCheck(this.coords, move))
                .collect(Collectors.toList());
    }

    @Override
    public final boolean isEmpty() { return false; }
    @Override
    public final boolean isCurrentlyHostile() { return this.color != this.getBoard().getCurrentColor(); }
    @Override
    public final boolean isCurrentlyCapturable() { return this.color != this.getBoard().getCurrentColor(); }
}
