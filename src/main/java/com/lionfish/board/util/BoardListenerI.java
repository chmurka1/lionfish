package com.lionfish.board.util;

import com.lionfish.board.Piece;

import java.util.List;

public interface BoardListenerI {
    char notifyPromotion(PieceColor color);
    void notifyCheckAt(Coords coords);
    void notifyMove(Coords targ, Coords dest, Piece[][] pieces, BoardSerializable boardSerializable);
    void notifyMoveRequestResponse(List<Coords> coords, Coords chosenCoords);

    void notifyWithdrawMove();

    void notifyCheckmate(PieceColor color);
    void notifyDraw();

    void notifyUpdate(Coords targ, Coords dest, Piece[][] pieces);
}
