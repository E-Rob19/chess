package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage{
    String errorMessage;

    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }

//    @Override
//    public String getMessageJSON() {
//        var serializer = new Gson();
//        return serializer.toJson(this);
//    }
}
