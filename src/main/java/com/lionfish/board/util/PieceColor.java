package com.lionfish.board.util;

public enum PieceColor {
    COLOR_WHITE,
    COLOR_BLACK;

    public PieceColor getOpposite() {
        return (this == COLOR_WHITE)?COLOR_BLACK:COLOR_WHITE;
    }
}
