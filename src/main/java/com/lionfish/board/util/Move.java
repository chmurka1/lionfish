package com.lionfish.board.util;

public class Move {
    final Coords targ, dest;

    public Move(Coords targ, Coords dest) {
        this.targ = targ;
        this.dest = dest;
    }

    public Coords getTarg() {
        return this.targ;
    }

    public Coords getDest() {
        return this.dest;
    }
}
