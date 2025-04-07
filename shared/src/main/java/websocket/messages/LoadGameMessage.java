package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage{

    ChessGame game;

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getMessage() {
        return game;
    }

//    @Override
//    public String getMessageJSON() {
//        var serializer = new Gson();
//        return serializer.toJson(this);
//    }
}
