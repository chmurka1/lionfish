package com.lionfish.board.util;

import com.lionfish.board.Piece;

import java.util.List;

public interface BoardListenerI {
    char notifyPromotion(PieceColor color);
    void notifyCheckAt(Coords coords);
    void notifyReady(Piece[][] pieces);
    void notifyMove(Coords targ, Coords dest, Piece[][] pieces);
    void notifyMoveRequestResponse(List<Coords> coords, Coords chosenCoords);

    void notifyWithdrawMove();

    void notifyCheckmate(PieceColor color);
    void notifyDraw();
}
