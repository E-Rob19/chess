package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class PrintChessBoard {
    private ChessBoard board;

    public void printBack(ChessGame game){
        if (game == null){
            board = new ChessBoard();
            board.resetBoard();
        } else {
            board = game.getBoard();
        }
        ChessPiece piece = board.getPiece(new ChessPosition(1,1));
        String[] letters = {"h", "g", "f", "e", "d", "c", "b", "a"};
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.EMPTY);
        for (int i = 0; i < 8; i++){
            System.out.print(" " + letters[i] + " ");
            if(i%2==1){
                System.out.print(" ");
            }
        }
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
        for(int i = 1; i < 9; i++){
            printHelper(i, piece);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.EMPTY);
        for (int i = 0; i < 8; i++){
            System.out.print(" " + letters[i] + " ");
            if(i%2==1){
                System.out.print(" ");
            }
        }
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
    }

    public void print(ChessGame game){
        if (game == null){
            board = new ChessBoard();
            board.resetBoard();
        } else {
            board = game.getBoard();
        }
        ChessPiece piece = board.getPiece(new ChessPosition(1,1));
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.EMPTY);
        for (int i = 0; i < 8; i++){
            System.out.print(" " + letters[i] + " ");
            if(i%2==1){
                System.out.print(" ");
            }
        }
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
        for(int i = 8; i > 0; i--){
            printHelper(i, piece);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.EMPTY);
        for (int i = 0; i < 8; i++){
            System.out.print(" " + letters[i] + " ");
            if(i%2==1){
                System.out.print(" ");
            }
        }
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
    }

    private void printHelper(int i, ChessPiece piece){
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(" "+i+" ");
        for(int j = 1; j < 9; j++){
            if((i+j)%2==0){
                System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_MAGENTA);
            }
            piece = board.getPiece(new ChessPosition(i,j));
            printPiece(piece);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(" "+i+" ");
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
    }

    public void printPiece(ChessPiece piece){
        if(piece == null){
            System.out.print(EscapeSequences.EMPTY);
        } else if(piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_KING);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print(EscapeSequences.WHITE_ROOK);
            }
        } else {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                System.out.print(EscapeSequences.BLACK_PAWN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                System.out.print(EscapeSequences.BLACK_BISHOP);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                System.out.print(EscapeSequences.BLACK_KING);
            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                System.out.print(EscapeSequences.BLACK_QUEEN);
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                System.out.print(EscapeSequences.BLACK_KNIGHT);
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                System.out.print(EscapeSequences.BLACK_ROOK);
            }
        }
    }
}
