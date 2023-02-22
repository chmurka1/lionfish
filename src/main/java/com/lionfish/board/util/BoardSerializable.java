package com.lionfish.board.util;
import java.io.Serializable;

public record BoardSerializable(String lastMoveTarg, String lastMoveDest,
                                boolean whiteCastlingLeft, boolean whiteCastlingRight,
                                boolean blackCastlingLeft, boolean blackCastlingRight,
                                int halfMoveClock, int moveClock,
                                String desc) implements Serializable {}
