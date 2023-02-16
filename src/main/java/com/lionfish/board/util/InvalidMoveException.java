package com.lionfish.board.util;

public class InvalidMoveException extends IllegalArgumentException {
    public InvalidMoveException(String msg) {
        super(msg);
    }
}
