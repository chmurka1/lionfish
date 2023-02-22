package com.lionfish.GUI.controls;

import com.lionfish.board.Board;
import com.lionfish.board.Piece;
import com.lionfish.board.util.*;
import com.lionfish.GUI.util.ImageLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.List;

public class TileGridView {
    private Board board;
    private final Button[][] tiles;
    private final ImageLoader imageLoader;

    private Coords chosen;
    private TileGridState state;

    private final boolean invert;

    private Coords getCorrespondingCoords(int i, int j) {
        if( invert )
            return new Coords(board.getWidth()-i-1,board.getHeight()-j-1);
        else return new Coords(i,j);
    }

    private Button getTileAt(int i, int j) {
        Coords c = this.getCorrespondingCoords(i,j);
        return tiles[c.x][c.y];
    }

    public void clear() {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                List<String>  styles = this.getTileAt(i, j).getStyleClass();
                while( !styles.get(styles.size()-1).equals("whiteTile") &&
                        !styles.get(styles.size()-1).equals("blackTile") ) {
                    styles.remove(styles.size()-1);
                }
            }
        }
    }

    public void markCheck(Coords square) {
        this.getTileAt(square.x,square.y).getStyleClass().add("redTile");
    }

    public void markLastMove(Coords targ, Coords dest) {
        if( targ == null || dest == null ) return;
        this.getTileAt(targ.x, targ.y).getStyleClass().add("greyTile");
        this.getTileAt(dest.x, dest.y).getStyleClass().add("greyTile");
    }

    public void markPossibleSquares(List<Coords> coords) {
        for( Coords c : coords ) {
            this.getTileAt(c.x, c.y).getStyleClass().add("greenTile");
        }
    }

    public void clearPossibleSquares() {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                this.getTileAt(i, j).getStyleClass().remove("greenTile");
            }
        }
    }

    public void reset() {
        this.state = TileGridState.IDLE;
        this.chosen = null;
    }

    public void refresh(Piece[][] pieces) {
        this.reset();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Coords c = getCorrespondingCoords(i, j);
                tiles[i][j].setGraphic(imageLoader.getResource(pieces[c.y][c.x].getTextureName()));
            }
        }
    }

    public void choosePiece(List<Coords> coords, Coords chosenCoords) {
        this.state = TileGridState.PIECE_CHOSEN;
        this.chosen = chosenCoords;
        markPossibleSquares(coords);
    }

    private void onClick(int i, int j) {
        clearPossibleSquares();
        if(this.state == TileGridState.IDLE) {
            this.board.moveRequest(getCorrespondingCoords(i,j));
        }
        else /* if( state == TileGridState.PIECE_CHOSEN) */ {
            try {
                this.board.move(chosen, getCorrespondingCoords(i, j));
            }
            catch(InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public TileGridView(GridPane gp, Board board, ImageLoader imageLoader) {
        this.board = board;
        this.tiles = new Button[board.getWidth()][board.getHeight()];
        this.imageLoader = imageLoader;
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                final int fi = i;
                final int fj = j;
                Button button = new Button();
                button.getStyleClass().add(((i+j)%2 == 1)?"whiteTile":"blackTile");

                button.setOnAction((ActionEvent e)->onClick(fi,fj));

                tiles[i][j] = button;
                gp.add(button, i, j);
            }
        }
        this.state = TileGridState.IDLE;
        this.invert = true;
    }

    public void setBoardState(BoardSerializable bs, BoardListenerI boardListener) {
        this.board = new Board(
                this.board.getCurrentColor(),
                this.board.getWidth(),
                this.board.getHeight(),
                bs.desc(),
                bs.whiteCastlingLeft(),
                bs.whiteCastlingRight(),
                bs.blackCastlingRight(),
                bs.whiteCastlingLeft(),
                bs.halfMoveClock(),
                bs.moveClock(),
                new Move(new Coords(bs.lastMoveTarg()), new Coords(bs.lastMoveDest())),
                boardListener
        );
    }
}
