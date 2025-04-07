package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
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


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDataAccess gameDAO = new SQLGameDAO();
    private AuthDataAccess authDAO = new SQLAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException, DataAccessException {
        connections.add(command.getAuthToken(), command.getGameID(), session);
        if(command.getGameID() <= gameDAO.listGames().size()) {
            if(authDAO.getAuthFromToken(command.getAuthToken())!=null) {
                String username = authDAO.getAuthFromToken(command.getAuthToken()).username();
                ChessGame game = gameDAO.getGame(command.getGameID()).game();
                LoadGameMessage action = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                connections.sendBack(command.getAuthToken(), action);
                var message = String.format("%s connected to the game", username);
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
        ChessMove move = command.getMove();
        ChessGame game = gameDAO.getGame(command.getGameID()).game();
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) game.validMoves(move.getStartPosition());
        boolean canMove = false;
        for(ChessMove i : validMoves){
            if(i.equals(move)){
                canMove = true;
            }
        }
        if(canMove){
            game.makeMove(move);
        }
    }

    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {

    }

    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {

    }
}
