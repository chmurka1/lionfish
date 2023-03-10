package com.lionfish.GUI;

import com.lionfish.GUI.controls.TileGridView;
import com.lionfish.GUI.util.CachedImageLoader;
import com.lionfish.GUI.util.ImageLoader;
import com.lionfish.GUI.util.PromotionData;
import com.lionfish.GUI.views.ChessboardView;
import com.lionfish.board.Board;
import com.lionfish.board.Piece;
import com.lionfish.board.util.*;
import com.lionfish.network.NetworkInterface;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ChessboardPresenter implements BoardListenerI {
    private static final String PATH = "views/ChessboardView.fxml";
    private static final int SIZE = 8;
    private final ChessboardView cv;
    private final PromotionPresenter whitePromotionPresenter, blackPromotionPresenter;
    private final TileGridView tileGridView;
    private final NetworkInterface networkInterface;

    /* board listener implementation */
    @Override
    public char notifyPromotion(PieceColor color) {
        Dialog<PromotionData> promotionDataDialog = this.getPromotionPresenter(color);

        Optional<PromotionData> result = promotionDataDialog.showAndWait();
        return result.map(PromotionData::getChar).orElse('*');
    }
    @Override
    public void notifyCheckAt(Coords coords) {
        this.tileGridView.markCheck(coords);
    }
    @Override
    public void notifyMove(Coords targ, Coords dest, Piece[][] pieces, BoardSerializable boardSerializable) {
        this.notifyUpdate(targ, dest, pieces);
        this.tileGridView.sleep();
        Task<Void> task = new Task<>(){
            Object obj;
            @Override
            protected Void call(){
                ChessboardPresenter.this.networkInterface.setObject(boardSerializable);
                try {
                    this.obj = ChessboardPresenter.this.networkInterface.getObject();
                } catch(Exception e ) {
                    cancel();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                ChessboardPresenter.this.tileGridView.setBoardState(
                        (BoardSerializable) obj,
                        ChessboardPresenter.this);
                ChessboardPresenter.this.tileGridView.awake();
            }
        };
        new Thread(task).start();
    }
    @Override
    public void notifyCheckmate(PieceColor color) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Game is finished");
        if( color == PieceColor.COLOR_BLACK ) {
            dialog.setContentText("White wins");
        } else {
            dialog.setContentText("Black wins");
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        System.exit(0);
    }
    @Override
    public void notifyDraw() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Game is finished");
        dialog.setContentText("Game ended in a draw");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        System.exit(0);
    }
    @Override
    public void notifyMoveRequestResponse(List<Coords> coords, Coords chosenCoords) {
        this.tileGridView.clearPossibleSquares();
        this.tileGridView.choosePiece(coords, chosenCoords);
    }
    @Override
    public void notifyWithdrawMove() {
        this.tileGridView.reset();
    }

    @Override
    public void notifyUpdate(Coords targ, Coords dest, Piece[][] pieces) {
        this.tileGridView.clear();
        this.tileGridView.markLastMove(targ, dest);
        this.tileGridView.refresh(pieces);
    }

    public PromotionPresenter getPromotionPresenter(PieceColor color) {
        return (color == PieceColor.COLOR_WHITE)?whitePromotionPresenter:blackPromotionPresenter;
    }

    ChessboardPresenter(NetworkInterface networkInterface, PieceColor startingColor) {
        this.cv = this.getController();
        Board board = new Board(
                startingColor,
                SIZE,
                SIZE,
                "RNBKQBNRPPPPPPPP********************************pppppppprnbkqbnr",
                true,
                true,
                true,
                true,
                0,
                0,
                new Move(new Coords(-1,-1), new Coords(-1,-1)),
                this
        );
        ImageLoader imageLoader = new CachedImageLoader("/com/lionfish/GUI/textures");
        this.tileGridView = new TileGridView(cv.boardGrid, board, imageLoader);
        this.whitePromotionPresenter = new PromotionPresenter(PieceColor.COLOR_WHITE, imageLoader);
        this.blackPromotionPresenter = new PromotionPresenter(PieceColor.COLOR_BLACK, imageLoader);
        this.networkInterface = networkInterface;

        board.setup();
        if(startingColor == PieceColor.COLOR_BLACK){
            this.tileGridView.sleep();
            Task<Void> task = new Task<>(){
                Object obj;
                @Override
                protected Void call(){
                    try {
                        obj = ChessboardPresenter.this.networkInterface.getObject();
                    } catch (Exception e) {
                        cancel();
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    ChessboardPresenter.this.tileGridView.setBoardState(
                            (BoardSerializable) obj,
                            ChessboardPresenter.this);
                    ChessboardPresenter.this.tileGridView.awake();
                }
            };
            new Thread(task).start();
        }
    }

    public Parent getRoot() {
        return cv.pane;
    }

    private ChessboardView getController() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(PATH));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }
}
