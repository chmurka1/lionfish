package com.lionfish.GUI;

import com.lionfish.board.util.PieceColor;
import com.lionfish.GUI.util.ImageLoader;
import com.lionfish.GUI.util.PromotionData;
import com.lionfish.GUI.views.PromotionView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PromotionPresenter extends Dialog<PromotionData> {
    public static final String PATH = "views/PromotionView.fxml";
    public static final int PIECE_COUNT = 4;

    private final PieceColor color;
    private final ImageLoader imageLoader;

    private void buildPiecePicker(PromotionView promotionView) {
        List<String> pieceTextureNames = (this.color == PieceColor.COLOR_WHITE) ? Arrays.asList("Nw","Bw","Rw","Qw") : Arrays.asList("nb","bb","rb","qb");
        List<Character> pieceNames = (this.color == PieceColor.COLOR_WHITE) ? Arrays.asList('N','B','R','Q') : Arrays.asList('n','b','r','q');
        for (int i = 0; i < PIECE_COUNT; i++) {
            final int fi = i;
            Button button = new Button();
            button.getStyleClass().add((i % 2 == 1) ? "whiteTile" : "blackTile");
            button.setGraphic(imageLoader.getResource(pieceTextureNames.get(i)));

            button.setOnAction(e -> {
                e.consume();
                this.setResult(new PromotionData(pieceNames.get(fi)));
            });

            promotionView.boardGrid.add(button, i, 0);
        }
    }

    public PromotionPresenter(PieceColor color, ImageLoader imageLoader) {
        this.color = color;
        this.imageLoader = imageLoader;
        PromotionView promotionView = getController();
        this.buildPiecePicker(promotionView);
        this.getDialogPane().setContent(promotionView.pane);
    }

    private PromotionView getController() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(PATH));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }
}
