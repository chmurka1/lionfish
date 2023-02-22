package com.lionfish.board;

import com.lionfish.board.util.*;

public class Board {
    private final int width;
    private final int height;
    private PieceColor color;
    private Move lastMove;
    private boolean whiteCastlingLeft;
    private boolean whiteCastlingRight;
    private boolean blackCastlingLeft;
    private boolean blackCastlingRight;
    private int halfMoveClock;
    private int moveClock;
    private final PieceFactory pf;

    private final Piece[][] pieces;

    private final BoardListenerI listener;

    private Coords whiteKingCoords, blackKingCoords;
    /*
     * use with caution, possible breach of class constraints
     */
    private void setKing(PieceColor color, Coords coords) {
        if( color == PieceColor.COLOR_WHITE ) {
            whiteKingCoords = coords;
        }
        else {
            blackKingCoords = coords;
        }
    }

    /*
     * assumes castling is legal
     */
    private void castle(Coords targ, Coords dest ) {
            if( targ.equals(new Coords(3,0))) { /* white castling */
                if (dest.equals(new Coords(1, 0))) {
                    this.setPiece(pf.getBySymbol('K'), 1, 0);
                    this.setPiece(pf.getBySymbol('R'), 2, 0);
                    this.setPiece(pf.getNull(),3, 0);
                    this.setPiece(pf.getNull(), 0, 0);
                } else if (dest.equals(new Coords(5, 0))) {
                    this.setPiece(pf.getBySymbol('K'), 5, 0);
                    this.setPiece(pf.getBySymbol('R'), 4, 0);
                    this.setPiece(pf.getNull(), 3, 0);
                    this.setPiece(pf.getNull(), 7, 0);
                }
            } else if( targ.equals(new Coords(3,7))) { /* black castling */
                if (dest.equals(new Coords(1, 7))) {
                    this.setPiece(pf.getBySymbol('k'), 1, 7);
                    this.setPiece(pf.getBySymbol('r'), 2, 7);
                    this.setPiece(pf.getNull(),3, 7);
                    this.setPiece(pf.getNull(),0, 7);
                } else if (dest.equals(new Coords(5, 7))) {
                    this.setPiece(pf.getBySymbol('k'), 5, 7);
                    this.setPiece(pf.getBySymbol('r'), 4, 7);
                    this.setPiece(pf.getNull(),3, 7);
                    this.setPiece(pf.getNull(),7, 7);
                }
            }
    }
    private void promote(Coords coords) throws RuntimeException {
        char sym = this.listener.notifyPromotion(this.getCurrentColor());
        this.setPiece(pf.getBySymbol(sym), coords);
    }

    /**
     * @param width width of a board
     * @param height height of a board
     * @param desc description of starting position in FEN format
     * @param boardListener listener handling all board state changes
     */
    public Board(PieceColor color, int width, int height, String desc, boolean wcl, boolean wcr, boolean bcr, boolean bcl, int hmc, int mc, Move lastMove, BoardListenerI boardListener) {
        this.color = PieceColor.COLOR_WHITE;
        this.width = width;
        this.height = height;
        this.whiteCastlingLeft = wcl;
        this.whiteCastlingRight = wcr;
        this.blackCastlingRight = bcr;
        this.blackCastlingLeft = bcl;
        this.halfMoveClock = hmc;
        this.moveClock = mc;
        this.lastMove = lastMove;

        this.pieces = new Piece[width][height];
        this.pf = new PieceFactory();
        Piece whiteKingTmp = pf.getNull();
        Piece blackKingTmp = pf.getNull();
        for(int i = 0; i < this.height; i++ ) {
            for(int j = 0; j < this.width; j++ ) {
                pieces[i][j] = pf.getBySymbol(desc.charAt(i*this.width+j));
                if( pieces[i][j].isKing() ) {
                    if( pieces[i][j].isCurrentlyHostile(this) ) {
                        if( !blackKingTmp.isEmpty() )
                            throw new IllegalArgumentException("Board contains more than one black king");
                        blackKingTmp = pieces[i][j];
                        blackKingCoords = new Coords(j,i);
                    }
                    else {
                        if( !whiteKingTmp.isEmpty() )
                            throw new IllegalArgumentException("Board contains more than one white king");
                        whiteKingTmp = pieces[i][j];
                        whiteKingCoords = new Coords(j, i);
                    }
                }
            }
        }
        if( blackKingTmp.isEmpty() )
            throw new IllegalArgumentException("Board contains no black king");
        if( whiteKingTmp.isEmpty() )
            throw new IllegalArgumentException("Board contains no white king");

        this.listener = boardListener;
        this.color = color;

        //this.listener.notifyUpdate(lastMove.getTarg(), lastMove.getDest(), pieces);
    }

    /* general info and utility */
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public PieceColor getCurrentColor() {
        return color;
    }
    public boolean containsCoords(Coords c) {
        return c.isInBounds(0,0, width -1, height -1);
    }

    /* grid data */
    private void setPiece(Piece piece, int x, int y) {
        setPiece(piece, new Coords(x, y));
    }
    private void setPiece(Piece piece, Coords coords) {
        if( !this.containsCoords(coords) ) {
            throw new IllegalArgumentException("Index of piece out of bounds");
        }
        pieces[coords.getRow()][coords.getColumn()] = piece;
    }
    Piece getPieceAt(Coords coords) {
        if( !this.containsCoords(coords)) {
            throw new IllegalArgumentException("Requested piece out of bounds");
        }
        return pieces[coords.getRow()][coords.getColumn()];
    }

    public void setup() {
        this.listener.notifyUpdate(lastMove.getTarg(),lastMove.getDest(),pieces);
    }

    /**
     * @param coords coordinates of chosen piece
     */
    public void moveRequest(Coords coords) {
        if(!this.getPieceAt(coords).isCurrentlyHostile(this)) {
            this.listener.notifyMoveRequestResponse(
                    this.getPieceAt(coords).getAvailableSquares(this, coords),
                    coords
            );
        }
    }
    /**
     * @param targ target piece
     * @param dest destination of target piece
     * @throws IllegalArgumentException if the move is illegal
     */
    public void move(Coords targ, Coords dest) {
        Piece movingPiece = this.getPieceAt(targ);
        Piece capturedPiece = this.getPieceAt(dest);
        try {
            if (movingPiece.isEmpty())
                throw new InvalidMoveException("You cannot move empty piece");
            if (movingPiece.isCurrentlyHostile(this))
                throw new InvalidMoveException("You cannot move hostile piece");
            if (!movingPiece.getAvailableSquares(this, targ).contains(dest))
                throw new InvalidMoveException("You cannot move piece to unavailable square");
        } catch (InvalidMoveException e) {
            this.listener.notifyWithdrawMove();
            throw e;
        }

        Piece copy = pf.getBySymbol(movingPiece.getSymbol());
        /* special case -- castling */
        if( copy.isKing() &&
                ( targ.sub(dest).getColumn() == 2 || targ.sub(dest).getColumn() == -2 )
        ) {
            castle(targ, dest);
        } else {
            for (Coords capt : movingPiece.getCaptureSquares(this,targ,dest)) {
                this.setPiece(pf.getNull(),capt);
            }
            this.setPiece(pf.getNull(), targ);
            this.setPiece(copy, dest);
        }
        if(copy.isKing()) {
            setKing(this.color, dest);
        }
        if(copy.isPromotable(this, dest)) {
            this.promote(dest);
        }

        /* modify castling status */
        if( targ.equals(new Coords(0,0))) {
            this.whiteCastlingRight = false;
        } else if( targ.equals(new Coords(7,0))) {
            this.whiteCastlingLeft = false;
        } else if( targ.equals(new Coords(0,7))) {
            this.blackCastlingRight = false;
        } else if( targ.equals(new Coords(7,7))) {
            this.blackCastlingLeft = false;
        } else if( movingPiece.isKing() ) {
            if( this.getCurrentColor() == PieceColor.COLOR_WHITE ) {
                this.whiteCastlingLeft = false;
                this.whiteCastlingRight = false;
            } else if( this.getCurrentColor() == PieceColor.COLOR_BLACK ) {
                this.blackCastlingLeft = false;
                this.blackCastlingRight= false;
            }
        }

        /* update clocks */
        this.moveClock++;
        if( movingPiece.isResettingFiftyRuleMove() || !capturedPiece.isEmpty() )
            this.halfMoveClock = 0;
        else
            this.halfMoveClock++;

        /* change player */
        //this.color = this.color.getOpposite();
        this.lastMove = new Move(targ, dest);

        /* notify about move, checks, or game resolution */
        this.listener.notifyMove(
                targ,
                dest,
                pieces,
                new BoardSerializable(
                        targ.toString(),
                        dest.toString(),
                        whiteCastlingLeft,
                        whiteCastlingRight,
                        blackCastlingLeft,
                        blackCastlingRight,
                        halfMoveClock,
                        moveClock,
                        this.getDesc()
                ));
        boolean check = this.isCheck();
        boolean stalemate = this.isStalemate();
        if( check && stalemate )
            this.listener.notifyCheckmate(this.getCurrentColor());
        else if( check )
            this.listener.notifyCheckAt(this.getKingCoords());
        else if( stalemate || halfMoveClock > 50 )
            this.listener.notifyDraw();
    }

    /* miscellaneous */
    /**
     * @return true if current player cannot move, false otherwise
     */
    boolean isStalemate() {
        for( int i = 0; i < this.getWidth(); i++ )
            for( int j = 0; j < this.getHeight(); j++ )
                if( !pieces[i][j].isCurrentlyHostile(this) && !pieces[i][j].getAvailableSquares(this, new Coords(j,i)).isEmpty() )
                    return false;
        return true;
    }
    /**
     * @return true if current king is attacked, false otherwise
     */
    boolean isCheck() {
        return isAttacked(getKingCoords());
    }
    /**
     * @param targ target square
     * @param dest destination square
     * @return true if current king is attacked after moving piece from targ to dest, false otherwise
     */
    boolean peekIsNotCheck(Coords targ, Coords dest) {
        /* peek */
        Piece tmpTarg = this.getPieceAt(targ);
        Piece tmpDest = this.getPieceAt(dest);
        Coords tmpKingCoords = this.getKingCoords();

        Piece copy = pf.getBySymbol(tmpTarg.getSymbol());

        this.setPiece(pf.getNull(), targ);
        this.setPiece(copy, dest);
        if( copy.isKing() ) {
            this.setKing(this.color, dest);
        }
        /* check */
        boolean res = this.isCheck();
        /* reverse */
        this.setPiece(tmpTarg, targ);
        this.setPiece(tmpDest, dest);
        this.setKing(this.color, tmpKingCoords);
        return !res;
    }
    /**
     * @param coords coordinates of the queried square
     * @return true if the square is attacked, false otherwise
     */
    boolean isAttacked(Coords coords) {
        for(int i = 0; i < height; i++ ) {
            for(int j = 0; j < width; j++ ) {
                if( pieces[i][j].isCurrentlyHostile(this) && pieces[i][j].getAttackedSquares(this,new Coords(j,i)).contains(coords) ) {
                    return true;
                }
            }
        }
        return false;
    }
    Move getLastMove() {
        return lastMove;
    }

    /**
     * @param targ target square
     * @param dest destination square
     * @return true if king can castle by moving from targ to dest, false otherwise
     */
    boolean isCastlingPossible(Coords targ, Coords dest) {
        if( this.isCheck() )
            return false;
        if( dest.equals(new Coords(1,0))) {
            return this.whiteCastlingRight &&
                    this.getPieceAt(new Coords(2, 0)).isEmpty() &&
                    this.getPieceAt(new Coords(1, 0)).isEmpty() &&
                    this.peekIsNotCheck(targ, new Coords(2, 0)) &&
                    this.peekIsNotCheck(targ, new Coords(1, 0));
        } else if( dest.equals(new Coords(5,0))) {
            return this.whiteCastlingLeft &&
                    this.getPieceAt(new Coords(6, 0)).isEmpty() &&
                    this.getPieceAt(new Coords(5, 0)).isEmpty() &&
                    this.getPieceAt(new Coords(4, 0)).isEmpty() &&
                    this.peekIsNotCheck(targ, new Coords(5, 0)) &&
                    this.peekIsNotCheck(targ, new Coords(4, 0));
        } else if( dest.equals(new Coords(1,7))) {
            return this.blackCastlingRight &&
                    this.getPieceAt(new Coords(2, 7)).isEmpty() &&
                    this.getPieceAt(new Coords(1, 7)).isEmpty() &&
                    this.peekIsNotCheck(targ, new Coords(2, 7)) &&
                    this.peekIsNotCheck(targ, new Coords(1, 7));
        } else if( dest.equals(new Coords(5,7))) {
            return this.blackCastlingLeft &&
                    this.getPieceAt(new Coords(6, 7)).isEmpty() &&
                    this.getPieceAt(new Coords(5, 7)).isEmpty() &&
                    this.getPieceAt(new Coords(4, 7)).isEmpty() &&
                    this.peekIsNotCheck(targ, new Coords(5, 7)) &&
                    this.peekIsNotCheck(targ, new Coords(4, 7));
        }
        return false;
    }

    Coords getKingCoords() {
        return color == PieceColor.COLOR_WHITE?whiteKingCoords:blackKingCoords;
    }

    private String getDesc() {
        StringBuilder resBuilder = new StringBuilder();
        for(int i = 0; i < this.height; i++ ) {
            for(int j = 0; j < this.width; j++ ) {
                resBuilder.append(pieces[i][j].getSymbol());
            }
        }
        return resBuilder.toString();
    }
}
