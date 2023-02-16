package com.lionfish.board;

import com.lionfish.board.util.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Pawn extends MovablePiece {
    private static final List<Coords> white_captures = Arrays.asList(
            new Coords(-1, 1),
            new Coords(1, 1)
    );

    private static final List<Coords> black_captures = Arrays.asList(
            new Coords(-1, -1),
            new Coords(1, -1)
    );

    private static final List<Coords> white_starting_moves = Arrays.asList(
            new Coords(0, 1),
            new Coords(0, 2)
    );

    private static final List<Coords> black_starting_moves = Arrays.asList(
            new Coords(0, -1),
            new Coords(0, -2)
    );

    private static final List<Coords> white_moves = Collections.singletonList(
            new Coords(0, 1)
    );

    private static final List<Coords> black_moves = Collections.singletonList(
            new Coords(0, -1)
    );

    private List<Coords> getCaptures() { return color == PieceColor.COLOR_WHITE?white_captures:black_captures; }
    private List<Coords> getMoves(Board board, Coords coords) {
        /* avoid jumping over the piece */
        //TODO: account for coordinates out of bounds -- currently not necessary
        if( this.color == PieceColor.COLOR_WHITE && !board.getPieceAt(coords.add(white_moves.get(0))).isEmpty() ) {
            return Collections.emptyList();
        }
        if( this.color == PieceColor.COLOR_BLACK && !board.getPieceAt(coords.add(black_moves.get(0))).isEmpty() ) {
            return Collections.emptyList();
        }
        return this.getRawMoves(board, coords);
    }

    private List<Coords> getRawMoves(Board board, Coords coords) {
        if( this.isStarting(board, coords) ) {
            return (color == PieceColor.COLOR_WHITE)? white_starting_moves : black_starting_moves;
        } else {
            return (color == PieceColor.COLOR_WHITE)? white_moves : black_moves;
        }
    }

    private boolean isStarting(Board board, Coords coords) {
        if( this.color == PieceColor.COLOR_WHITE ) {
            return coords.getRow() == 1;
        }
        else /* if(this.color == PieceColor.COLOR_BLACK) */ {
            return coords.getRow() == board.getHeight() - 2;
        }
    }

    Pawn(PieceColor color) { super(color); }

    @Override
    protected List<Coords> getAvailableSquaresPrimitive(Board board, Coords coords) {
        return Stream.concat(
                this.getAttackedSquares(board, coords).stream()
                        .filter(move->board.getPieceAt(move).isCurrentlyCapturable(board)), /* captures */
                Stream.concat(
                        this.getAttackedSquares(board, coords).stream()
                                .filter(move->this.getCaptureSquares(board, coords,move).size() > 1), /* en passant captures */
                        this.getMoves(board, coords).stream()
                                .map(coords::add)
                                .filter(board::containsCoords) /* pushing moves */
                                .filter(move->!board.getPieceAt(move).isCurrentlyCapturable(board))
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<Coords> getAttackedSquares(Board board, Coords coords) {
        return this.getCaptures().stream()
                .map(coords::add)
                .filter(board::containsCoords)
                .collect(Collectors.toList());
    }
    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'P':'p'; }

    @Override
    public boolean isPromotable(Board board, Coords coords) {
        if( color == PieceColor.COLOR_WHITE )
            return coords.getRow() == board.getHeight() - 1;
        else return coords.getRow() == 0;
    }

    @Override
    public boolean canBeCapturedEnPassant(Board board, Coords coords) {
        Move lm = board.getLastMove();
        Coords disp = lm.getTarg().sub(coords);
        return lm.getDest().equals(coords)
                && ( disp.getRow() == 2 || disp.getRow() == -2 );
    }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Pw":"pb"; }

    @Override
    public List<Coords> getCaptureSquares(Board board, Coords targ, Coords dest) {
        Coords enp = dest.sub(this.getRawMoves(board, targ).get(0));
        if( board.getPieceAt(enp).canBeCapturedEnPassant(board, enp)) {
            return Arrays.asList(dest, dest.sub(this.getMoves(board, targ).get(0)));
        } else {
            return Collections.singletonList(dest);
        }
    }

    @Override
    public boolean isResettingFiftyRuleMove() {
        return true;
    }
}
