package com.lionfish.board.util;

/**
 * This parser interprets following extension of FEN format:
 * [first row description]/.../[last row description] [current color:w/b] [castling rights:KQkq] [en passant field] [half move clock] [move clock] [last move target] [last move destination]
 * e. g. rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 - - is a starting position
 */
public class FENParser {
    /*char[][] pieces;
    boolean[] castlingRights;

    public FENParser(int width, int height, String s) throws IOException {
        String[] tokens = s.split(" ");
        if(tokens.length != 8) {
            throw new IOException("Invalid FEN format");
        }

        //parse pieces
*/
        /* parse castling rights */
        /*castlingRights = new boolean[4];
        if( tokens[2].equals("-") ) {
            for( int i = 0; i < 4; i++ ) {
                castlingRights[i] = false;
            }
        }
        else {
            char[] syms = new char[]{'K', 'Q', 'k', 'q'};
            int j = 0;
            for (int i = 0; i < 4; i++) {
                if ( tokens[2].charAt(j) == syms[i]) {
                    castlingRights[i] = true;
                    j++;
                }
                else {
                    castlingRights[i] = false;
                }
            }
            if (j != tokens[2].length()) {
                throw new IOException("Invalid FEN format - 3rd token");
            }
        }



    }

    public char getPieceSymbolAt(int i, int j) {
        return pieces[i][j];
    }

    public boolean getCastlingRightK() {
        return castlingRights[0];
    }
    public boolean getCastlingRightQ() {
        return castlingRights[1];
    }
    public boolean getCastlingRightk() {
        return castlingRights[2];
    }
    public boolean getCastlingRightq() {
        return castlingRights[3];
    }*/
}
