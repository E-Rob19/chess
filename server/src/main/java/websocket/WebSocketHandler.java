package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDataAccess gameDAO = new SQLGameDAO();
    private AuthDataAccess authDAO = new SQLAuthDAO();
    private ArrayList<Integer> gameOverList = new ArrayList<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> {
                MakeMoveCommand moveCmd = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(moveCmd, session);
            }
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.add(command.getAuthToken(), command.getGameID(), session);
        gameOverList.remove(command.getGameID());
        if(command.getGameID() <= gameDAO.listGames().size()) {
            if(authDAO.getAuthFromToken(command.getAuthToken())!=null) {
                String username = authDAO.getAuthFromToken(command.getAuthToken()).username();
                ChessGame game = gameDAO.getGame(command.getGameID()).game();
                LoadGameMessage action = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                connections.sendBack(command.getAuthToken(), action);
                String message = "";
                if(Objects.equals(gameDAO.getGame(command.getGameID()).whiteUsername(), username)) {
                    message = String.format("%s connected to the game as the white player", username);
                } else if (Objects.equals(gameDAO.getGame(command.getGameID()).blackUsername(), username)) {
                    message = String.format("%s connected to the game as the black player", username);
                } else {
                    message = String.format("%s connected to the game as an observer", username);
                }
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            } else {
                ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Unauthorized");
                connections.sendBack(command.getAuthToken(), error);
            }
        } else {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Bad Game ID");
            connections.sendBack(command.getAuthToken(), error);
        }


    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException {
        if(authDAO.getAuthFromToken(command.getAuthToken())==null) {
            connections.add(command.getAuthToken(), command.getGameID(), session);
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Unauthorized");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        ChessMove move = command.getMove();
        ChessGame game = gameDAO.getGame(command.getGameID()).game();
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        String whiteUsername = gameDAO.getGame(command.getGameID()).whiteUsername();
        String blackUsername = gameDAO.getGame(command.getGameID()).blackUsername();
        String username = authDAO.getAuthFromToken(command.getAuthToken()).username();
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) game.validMoves(move.getStartPosition());
        ChessGame.TeamColor color = game.getTeamTurn();
        boolean canMove = false;
        boolean check = false;
        if(gameOverList.contains(command.getGameID())){
            check = true;
        }
        if(check) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "The game is over");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        if (!Objects.equals(username, whiteUsername) && !Objects.equals(username, blackUsername)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Must be a player to make moves");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        boolean whiteCheck = (Objects.equals(username, whiteUsername) && color != ChessGame.TeamColor.WHITE);
        if ( whiteCheck || (Objects.equals(username, blackUsername) && color != ChessGame.TeamColor.BLACK)){
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not your turn");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        whiteCheck = (color == ChessGame.TeamColor.WHITE && piece.getTeamColor() != ChessGame.TeamColor.WHITE);
        if ( whiteCheck || (color == ChessGame.TeamColor.BLACK && piece.getTeamColor() != ChessGame.TeamColor.BLACK)){
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not your piece");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        for (ChessMove i : validMoves) {
            if (i.equals(move)) {
                canMove = true;
                break;
            }
        }
        if (canMove) {
            game.makeMove(move);
            gameDAO.getGame(command.getGameID()).game().makeMove(move);
            ChessGame.TeamColor nextColor = game.getTeamTurn();
            if(color == ChessGame.TeamColor.WHITE) {
                game.setTeamTurn(ChessGame.TeamColor.BLACK);
                nextColor = ChessGame.TeamColor.BLACK;
            } else {
                game.setTeamTurn(ChessGame.TeamColor.WHITE);
                nextColor = ChessGame.TeamColor.WHITE;
            }
            gameDAO.updateGame(command.getGameID(), game, nextColor);
            LoadGameMessage action = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.sendBack(command.getAuthToken(), action);
            connections.broadcast(command.getAuthToken(), command.getGameID(), action);
            String start = positionConvert(move.getStartPosition());
            var message = String.format("%s moved %s to %s", username, start, positionConvert(move.getEndPosition()));
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                gameDAO.getGame(command.getGameID()).game().setGameOver(true);
                gameOverList.add(command.getGameID());
                message = String.format("%s is in checkmate!", gameDAO.getGame(command.getGameID()).whiteUsername());
                notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.sendBack(command.getAuthToken(), notification);
                connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)){
                gameDAO.getGame(command.getGameID()).game().setGameOver(true);
                gameOverList.add(command.getGameID());
                message = String.format("%s is in checkmate!", gameDAO.getGame(command.getGameID()).blackUsername());
                notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.sendBack(command.getAuthToken(), notification);
                connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            } else if(game.isInCheck(ChessGame.TeamColor.WHITE)){
                message = String.format("%s is in check!", gameDAO.getGame(command.getGameID()).whiteUsername());
                notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.sendBack(command.getAuthToken(), notification);
                connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)){
                message = String.format("%s is in check!", gameDAO.getGame(command.getGameID()).blackUsername());
                notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
                connections.sendBack(command.getAuthToken(), notification);
            }
        } else {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not a Valid Move");
            connections.sendBack(command.getAuthToken(), error);
        }

    }

    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.remove(command.getAuthToken());
        String username = authDAO.getAuthFromToken(command.getAuthToken()).username();
        String whiteUsername = gameDAO.getGame(command.getGameID()).whiteUsername();
        String blackUsername = gameDAO.getGame(command.getGameID()).blackUsername();
        String color = "";
        if (!username.equalsIgnoreCase(whiteUsername) && !username.equalsIgnoreCase(blackUsername)){
            var message = String.format("%s has left the game", username);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
            return;
        }
        if (username.equalsIgnoreCase(whiteUsername)){
            color = "white";
        } else if (username.equalsIgnoreCase(blackUsername)) {
            color = "black";
        }
        gameDAO.addPlayer(command.getGameID(), null, color);
        var message = String.format("%s has left the game", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        //connections.sendBack(command.getAuthToken(), notification);
        connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
    }

    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {

        String username = authDAO.getAuthFromToken(command.getAuthToken()).username();
        String whiteUsername = gameDAO.getGame(command.getGameID()).whiteUsername();
        String blackUsername = gameDAO.getGame(command.getGameID()).blackUsername();
        ChessGame game = gameDAO.getGame(command.getGameID()).game();
        if (!Objects.equals(username, whiteUsername) && !Objects.equals(username, blackUsername)) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Must be a player to make moves");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        boolean check = false;
        if(gameOverList.contains(command.getGameID())){
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "The game is over");
            connections.sendBack(command.getAuthToken(), error);
            return;
        }
        gameDAO.getGame(command.getGameID()).game().setGameOver(true);
        gameOverList.add(command.getGameID());
        var message = String.format("%s has resigned the game", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendBack(command.getAuthToken(), notification);
        connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        return gameDAO.listGames();
    }

    private String positionConvert(ChessPosition position){
        String pos = "";
        int col = position.getColumn();
        int row = position.getRow();

        if(col == 1){
            pos += "a";
        } else if (col == 2) {
            pos += "b";
        } else if (col == 3) {
            pos += "c";
        }else if (col == 4) {
            pos += "d";
        }else if (col == 5) {
            pos += "e";
        }else if (col == 6) {
            pos += "f";
        }else if (col == 7) {
            pos += "g";
        }else if (col == 8) {
            pos += "h";
        }
        pos += row;
        return pos;
    }
}
