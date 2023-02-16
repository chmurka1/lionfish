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
    private List<Coords> getMoves() {
        /* avoid jumping over the piece */
        //TODO: account for coordinates out of bounds -- currently not necessary
        if( this.color == PieceColor.COLOR_WHITE && this.getBoard().getPieceAt(coords.add(white_moves.get(0))).isCurrentlyCapturable() ) {
            return Collections.emptyList();
        }
        if( this.color == PieceColor.COLOR_BLACK && this.getBoard().getPieceAt(coords.add(black_moves.get(0))).isCurrentlyCapturable() ) {
            return Collections.emptyList();
        }
        return this.getRawMoves();
    }

    private List<Coords> getRawMoves() {
        if( this.isStarting() ) {
            return (color == PieceColor.COLOR_WHITE)? white_starting_moves : black_starting_moves;
        } else {
            return (color == PieceColor.COLOR_WHITE)? white_moves : black_moves;
        }
    }

    private boolean isStarting() {
        if( this.color == PieceColor.COLOR_WHITE ) {
            return this.coords.getRow() == 1;
        }
        else /* if(this.color == PieceColor.COLOR_BLACK) */ {
            return this.coords.getRow() == this.getBoard().getHeight() - 2;
        }
    }

    Pawn(Board board, Coords coords, PieceColor color) { super(board, coords, color); }

    @Override
    protected List<Coords> getAvailableSquaresPrimitive() {
        return Stream.concat(
                this.getAttackedSquares().stream()
                        .filter(move->this.getBoard().getPieceAt(move).isCurrentlyCapturable()), /* captures */
                Stream.concat(
                        this.getAttackedSquares().stream()
                                .filter(move->this.getCaptureSquares(move).size() > 1), /* en passant captures */
                        this.getMoves().stream()
                                .map(this.coords::add)
                                .filter(this.getBoard()::containsCoords) /* pushing moves */
                                .filter(move->!this.getBoard().getPieceAt(move).isCurrentlyCapturable())
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<Coords> getAttackedSquares() {
        return this.getCaptures().stream()
                .map(this.coords::add)
                .filter(this.getBoard()::containsCoords)
                .collect(Collectors.toList());
    }
    @Override
    public char getSymbol() { return color == PieceColor.COLOR_WHITE?'P':'p'; }

    @Override
    public boolean isPromotable() {
        if( color == PieceColor.COLOR_WHITE )
            return this.coords.getRow() == this.getBoard().getHeight() - 1;
        else return this.coords.getRow() == 0;
    }

    @Override
    public boolean canBeCapturedEnPassant() {
        Move lm = this.getBoard().getLastMove();
        Coords disp = lm.getTarg().sub(this.getCoords());
        return lm.getDest().equals(this.getCoords())
                && ( disp.getRow() == 2 || disp.getRow() == -2 );
    }

    @Override
    public String getTextureName() { return color == PieceColor.COLOR_WHITE?"Pw":"pb"; }

    @Override
    public List<Coords> getCaptureSquares(Coords dest) {
        Coords enp = dest.sub(this.getRawMoves().get(0));
        if( this.getBoard().getPieceAt(enp).canBeCapturedEnPassant()) {
            return Arrays.asList(dest, dest.sub(this.getMoves().get(0)));
        } else {
            return Collections.singletonList(dest);
        }
    }

    @Override
    public boolean isResettingFiftyRuleMove() {
        return true;
    }
}
