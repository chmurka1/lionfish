package com.lionfish.GUI;

import com.lionfish.GUI.util.CachedImageLoader;
import com.lionfish.GUI.util.ImageLoader;
import com.lionfish.GUI.util.PromotionData;
import com.lionfish.board.Board;
import com.lionfish.board.Piece;
import com.lionfish.board.util.BoardListenerI;
import com.lionfish.board.util.Coords;
import com.lionfish.board.util.PieceColor;
import com.lionfish.GUI.controls.TileGridView;
import com.lionfish.GUI.views.ChessboardView;
import javafx.application.Platform;
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
    public void notifyMove(Coords targ, Coords dest, Piece[][] pieces) {
        this.tileGridView.clear();
        this.tileGridView.markLastMove(targ, dest);
        this.tileGridView.refresh(pieces);
    }
    @Override
    public void notifyCheckmate(PieceColor color) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Game is finished");
        dialog.setContentText("White wins");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        Platform.exit();
    }
    @Override
    public void notifyDraw() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Game is finished");
        dialog.setContentText("Game ended in a draw");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
        Platform.exit();
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
    public void notifyReady(Piece[][] pieces) {
        this.tileGridView.refresh(pieces);
    }

    public PromotionPresenter getPromotionPresenter(PieceColor color) {
        return (color == PieceColor.COLOR_WHITE)?whitePromotionPresenter:blackPromotionPresenter;
    }

    public ChessboardPresenter() {
        this.cv = this.getController();
        Board board = new Board(
                SIZE,
                SIZE,
                "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr",
                true,
                true,
                true,
                true,
                0,
                0,
                this
        );
        ImageLoader imageLoader = new CachedImageLoader("/com/lionfish/GUI/textures");
        this.tileGridView = new TileGridView(cv.boardGrid, board, imageLoader);
        this.whitePromotionPresenter = new PromotionPresenter(PieceColor.COLOR_WHITE, imageLoader);
        this.blackPromotionPresenter = new PromotionPresenter(PieceColor.COLOR_BLACK, imageLoader);
        board.setup();
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
