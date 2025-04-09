package websocket;

import websocket.messages.ServerMessage;

import java.util.zip.DataFormatException;

public interface NotificationHandler {
        void notify(ServerMessage notification, String message) throws DataFormatException;
}
