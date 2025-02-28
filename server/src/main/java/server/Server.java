package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import service.Service;
import spark.*;
import model.ErrorMessage;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.post("/db", this::clear);

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
        if(rReq.username() == null || rReq.password() == null || rReq.email()==null){
            res.status(400);
            return serializer.toJson(new ErrorMessage("Error: bad request"));
        }
        RegisterResult rRes = service.register(rReq);
        if(rRes == null){
            res.status(403);
            return serializer.toJson(new ErrorMessage("Error: already taken"));
        } else {
            return serializer.toJson(rRes);
        }
    }

    private Object clear(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

    }

}
