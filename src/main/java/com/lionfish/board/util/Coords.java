package com.lionfish.board.util;

import java.util.Objects;

import static java.lang.Integer.max;
import static java.lang.Math.abs;

public class Coords {
    private static final int ALPHABET_SIZE = 'z' - 'a' + 1;
    private static final char ALPHABET_START = 'a';
    public final int x, y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coords(String str) {
        if( str.length() != 2 || str.charAt(0) < ALPHABET_START || str.charAt(0) > ALPHABET_START + ALPHABET_SIZE
                || str.charAt(1) < '0' || str.charAt(1) > '9' ) {
            throw new IllegalArgumentException("Coordinates should consist of a letter and a digit.");
        }
        this.x = str.charAt(0)-ALPHABET_START;
        this.y = str.charAt(1)-'0';
    }

    public int getColumn() {
        return x;
    }

    public int getRow() {
        return y;
    }

    public String getColumnName() {
        if( this.x == 0 ) {
            return String.valueOf(ALPHABET_START);
        }

        int copy = abs(x);
        StringBuilder name = new StringBuilder();
        while(copy > 0) {
            name.append((char)(ALPHABET_START + copy % ALPHABET_SIZE));
            copy /= ALPHABET_SIZE;
        }
        if( x < 0 ) name.append('-');
        return name.reverse().toString();
    }

    public String getRowName() { return Integer.toString(y+1); }

    @SuppressWarnings("unused")
    public String getName() { return this.getColumnName() + this.getRowName(); }

    public Coords add(Coords rhs) { return new Coords(this.x + rhs.x, this.y + rhs.y); }
    public Coords sub(Coords rhs) { return new Coords(this.x - rhs.x, this.y - rhs.y); }
    /**
     * @return distance (by maximum metric) between this and rhs
     */
    @SuppressWarnings("unused")
    public int dist(Coords rhs) { return max(abs(this.x - rhs.x),abs(this.y - rhs.y)); }

    public boolean isInBounds(int left, int bottom, int right, int top) { return (left <= this.x && this.x <= right && bottom <= this.y && this.y <= top); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Coords rhs = (Coords) o;
        return Objects.equals(this.x, rhs.x) && Objects.equals(this.y, rhs.y);
    }

    @Override
    public String toString() {
        return this.getRowName() + this.getColumnName();
    }
}
