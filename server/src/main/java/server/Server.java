package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.RegisterRequest;
import service.RegisterResult;
import service.Service;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        Service service = new Service();
        var serializer = new Gson();
        //var  = new RegisterRequest();
        //var json =
        RegisterRequest rReq = serializer.fromJson(req.body(), RegisterRequest.class);
        RegisterResult rRes = service.register(rReq);
        return serializer.toJson(rReq);
    }

}
