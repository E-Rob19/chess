package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            //case EXIT -> exit(action.visitorName());
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        connections.add(command.getAuthToken(), command.getGameID(), session);
        var message = String.format("%s connected to the game", command.getAuthToken());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(command.getAuthToken(), command.getGameID(), notification);
        //make above notif a notif message and then send a actual load game message back to the root
        message = "is this where im supposed to get the board?";
        var action = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, message);
        connections.sendBack(command.getAuthToken(), action);
    }
}
