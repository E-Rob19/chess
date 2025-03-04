package chess;

import java.util.ArrayList;
//import java.util.Collection;

public class CalculateMoves {

    public CalculateMoves(){

    }

    public ArrayList<ChessMove> calulate(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        if (type == ChessPiece.PieceType.BISHOP){
            movs = bishopMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.KING) {
            movs = kingMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            movs = knightMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.PAWN) {
            movs = pawnMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.QUEEN) {
            movs = queenMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.ROOK) {
            movs = rookMoves(board, myPosition);
        }
        return movs;
    }

    private ChessMove checkSpace(ChessBoard board, ChessPosition myPosition, ChessPosition pos){
        if (board.getPiece(pos) != null){
            if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                return new ChessMove(myPosition, pos, null);
            } else { return null; }
        } else { return new ChessMove(myPosition, pos, null); }
    }
    private ChessMove checkSpace(ChessBoard board, ChessPosition myPosition, ChessPosition pos, ChessPiece.PieceType promotion){
        if (board.getPiece(pos) != null){
            if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                return new ChessMove(myPosition, pos, promotion);
            } else { return null; }
        } else { return new ChessMove(myPosition, pos, promotion); }
    }

    private ChessMove checkSpacePawn(ChessBoard board, ChessPosition myPosition, ChessPosition pos){
        if (board.getPiece(pos) != null){
            if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                return new ChessMove(myPosition, pos, null);
            } else { return null; }
        }
        return null;
    }
    private ChessMove checkSpacePawn(ChessBoard board, ChessPosition myPosition, ChessPosition pos, ChessPiece.PieceType promotion){
        if (board.getPiece(pos) != null){
            if(board.getPiece(pos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                return new ChessMove(myPosition, pos, promotion);
            } else { return null; }
        }
        return null;
    }

    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov;
        while (row < 8 && col < 8) {
            row++; col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1 && col > 1) {
            row--; col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1 && col < 8) {
            row--; col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row < 8 && col > 1) {
            row++; col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        return movs;
    }

    private ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov; ChessPosition pos;
        if (row>1 && row<8 && col>1 && col<8) {
            for(int i = -1; i < 2; i++) {
                pos = new ChessPosition(row+i, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
            for(int i = -1; i < 2; i++) {
                pos = new ChessPosition(row+i, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
            pos = new ChessPosition(row+1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
            pos = new ChessPosition(row-1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
        }
        if(row == 1){
            pos = new ChessPosition(row+1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
            if(col != 8){
                pos = new ChessPosition(row+1, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
            if (col != 1) {
                pos = new ChessPosition(row+1, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
        }
        if(row == 8){
            pos = new ChessPosition(row-1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
            if(col != 8){
                pos = new ChessPosition(row-1, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
            if (col != 1) {
                pos = new ChessPosition(row-1, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
        }
        if((col == 1 || col == 8) && row != 1 && row != 8){
            pos = new ChessPosition(row-1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
            pos = new ChessPosition(row+1, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov != null) { movs.add(mov); }
            if(col == 8){
                pos = new ChessPosition(row-1, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row+1, col-1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
            if (col == 1) {
                pos = new ChessPosition(row-1, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
                pos = new ChessPosition(row+1, col+1);
                mov = checkSpace(board, myPosition, pos);
                if (mov != null) { movs.add(mov); }
            }
        }
        return movs;
    }

    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int row1 = myPosition.getRow();
        int col = myPosition.getColumn();
        int col1 = myPosition.getColumn();
        ChessMove mov;
        row -= 2; row1 += 2; col -= 1; col1 += 1;
        knightHelper(row, row1, col, col1, movs, board, myPosition);
        if(row1<9) {
            if (col>0) {
                mov = checkSpace(board, myPosition, new ChessPosition(row1, col));
                if (mov != null) {movs.add(mov);}
            }
            if (col1<8) {
                mov = checkSpace(board, myPosition, new ChessPosition(row1, col1));
                if (mov != null) {movs.add(mov);}
            }
        }
        row += 1; row1 -= 1; col -= 1; col1 += 1;
        knightHelper(row, row1, col, col1, movs, board, myPosition);
        if(row1<9) {
            if (col>0) {
                mov = checkSpace(board, myPosition, new ChessPosition(row1, col));
                if (mov != null) {movs.add(mov);}
            }
            if (col1<9) {
                mov = checkSpace(board, myPosition, new ChessPosition(row1, col1));
                if (mov != null) {movs.add(mov);}
            }
        }
        return movs;
    }

    private void knightHelper(int row, int row1, int col, int col1, ArrayList<ChessMove> movs, ChessBoard board, ChessPosition myPosition){
        ChessMove mov;
        if(row>0) {
            if (col>0) {
                mov = checkSpace(board, myPosition, new ChessPosition(row, col));
                if (mov != null) {movs.add(mov);}
            }
            if (col1<9) {
                mov = checkSpace(board, myPosition, new ChessPosition(row, col1));
                if (mov != null) {movs.add(mov);}
            }
        }
    }

    private ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov; ChessPosition pos = new ChessPosition(row, col);
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(row < 7) {
                pos = new ChessPosition(row+1, col);
                if (board.getPiece(pos) == null){  movs.add(new ChessMove(myPosition, pos, null));}
                if (col < 7) {
                    pos = new ChessPosition(row + 1, col + 1);
                    mov = checkSpacePawn(board, myPosition, pos);
                    if (mov != null) { movs.add(mov); }
                }
                if (col > 1) {
                    pos = new ChessPosition(row + 1, col - 1);
                    mov = checkSpacePawn(board, myPosition, pos);
                    if (mov != null) { movs.add(mov); }
                }
                if (board.getPiece(myPosition).getinitialMove() && myPosition.getRow() == 2 && board.getPiece(new ChessPosition(row+1, col))==null){
                    pos = new ChessPosition(row + 2, col);
                    if (board.getPiece(pos) == null){ movs.add(new ChessMove(myPosition, pos, null)); }
                }
            }
            if (row == 7){
                pos = new ChessPosition(row+1, col);
                movs = pawnHelper(pos, myPosition, board, movs);
                if (col < 7) {
                    pos = new ChessPosition(row + 1, col + 1);
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.QUEEN);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.BISHOP);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.KNIGHT);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.ROOK);
                    if (mov != null) { movs.add(mov); }
                }
                if (col > 1) {
                    pos = new ChessPosition(row + 1, col - 1);
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.QUEEN);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.BISHOP);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.KNIGHT);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.ROOK);
                    if (mov != null) { movs.add(mov);}
                }
            }
        }
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if(row > 2) {
                pos = new ChessPosition(row-1, col);
                if (board.getPiece(pos) == null){ movs.add(new ChessMove(myPosition, pos, null)); }
                if (col < 7) {
                    pos = new ChessPosition(row - 1, col + 1);
                    mov = checkSpacePawn(board, myPosition, pos);
                    if (mov != null) { movs.add(mov); }
                }
                if (col > 1) {
                    pos = new ChessPosition(row - 1, col - 1);
                    mov = checkSpacePawn(board, myPosition, pos);
                    if (mov != null) { movs.add(mov); }
                }
                if (board.getPiece(myPosition).getinitialMove() && row == 7 && board.getPiece(new ChessPosition(row-1, col))==null){
                    pos = new ChessPosition(row - 2, col);
                    if (board.getPiece(pos) == null){ movs.add(new ChessMove(myPosition, pos, null)); }
                }
            }
            if (row == 2){
                pos = new ChessPosition(row-1, col);
                movs = pawnHelper(pos, myPosition, board, movs);
                if (col < 7) {
                    pos = new ChessPosition(row - 1, col + 1);
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.QUEEN);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.BISHOP);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.KNIGHT);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.ROOK);
                    if (mov != null) { movs.add(mov); }
                }
                if (col > 1) {
                    pos = new ChessPosition(row - 1, col - 1);
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.QUEEN);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.BISHOP);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.KNIGHT);
                    if (mov != null) { movs.add(mov); }
                    mov = checkSpacePawn(board, myPosition, pos, ChessPiece.PieceType.ROOK);
                    if (mov != null) { movs.add(mov);}
                }
            }
        }
        return movs;
    }

    private ArrayList<ChessMove> pawnHelper(ChessPosition pos, ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> movs){
        if (board.getPiece(pos) == null){
            movs.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.QUEEN));
            movs.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.BISHOP));
            movs.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.KNIGHT));
            movs.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.ROOK));
        }
        return movs;
    }

    private ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        movs.addAll(bishopMoves(board, myPosition));
        movs.addAll(rookMoves(board, myPosition));
        return movs;
    }

    private ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movs = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessMove mov;
        while (row < 8) {
            row++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (row > 1) {
            row--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col < 8) {
            col++;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col > 1) {
            col--;
            ChessPosition pos = new ChessPosition(row, col);
            mov = checkSpace(board, myPosition, pos);
            if (mov!= null){movs.add(mov);}
            if (board.getPiece(pos) != null){ break; }
        }
        return movs;
    }
}
