package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
//import exception.ResponseException;
//import dataaccess.DataAccessException;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class WebSocketFacade extends Endpoint{

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                        //System.out.print(message);
                        notificationHandler.notify(notification, message);
                    } catch (Exception ex) {
                        System.out.print("failed to receive message");
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void connect(String authToken, Integer gameID) throws IOException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws IOException {
        try {
            var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws IOException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws IOException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }



}
