package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teem = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teem;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teem = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece myPiece = board.getPiece(startPosition);
        ChessBoard testBoard = board.copy();
        ArrayList<ChessMove> invalidMoves = new ArrayList<ChessMove>();
        if(myPiece != null) {
            Collection<ChessMove> moves = myPiece.pieceMoves(board, startPosition);
            for(ChessMove move : moves){
                board.addPiece(startPosition, null);
                if(board.getPiece(move.getEndPosition()) != null) {
                    board.addPiece(move.getEndPosition(), null);
                }
                board.addPiece(move.getEndPosition(), myPiece);
                if(isInCheck(myPiece.getTeamColor())){
                    invalidMoves.add(move);
                }
                board = testBoard.copy();
            }
            for(ChessMove move : invalidMoves){
                moves.remove(move);
            }
            return moves;
        } else {
            return null;
        }
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece myPiece = null;
        ChessPosition kingPos = null;
        ChessPosition pos;
        for(int i  = 1; i < 9; i++){
            for(int j  = 1; j < 9; j++){
                pos = new ChessPosition(i,j);
                myPiece = board.getPiece(pos);
                if(myPiece != null) {
                    if (myPiece.getPieceType() == ChessPiece.PieceType.KING && myPiece.getTeamColor() == teamColor) {
                        kingPos = pos;
                    }
                }
            }
        }
        Collection<ChessMove> moves;
        for(int i  = 1; i < 9; i++){
            for(int j  = 1; j < 9; j++){
                pos = new ChessPosition(i,j);
                myPiece = board.getPiece(pos);
                if(myPiece != null) {
                    moves = myPiece.pieceMoves(board, pos);
                    for (ChessMove move : moves) {
                        ChessPosition checkPos = move.getEndPosition();
                        if(checkPos.equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
        //throw new RuntimeException("Not implemented");
    }
}
