package server;

import requests.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.Service;
import spark.*;
import model.ErrorMessage;

import java.util.ArrayList;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

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

        service.clear();

        return serializer.toJson(new Object());
    }

    private Object login(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

        LoginRequest rReq = serializer.fromJson(req.body(), LoginRequest.class);
        if(rReq.username() == null || rReq.password() == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }
        RegisterResult rRes = service.login(rReq);
        if(rRes == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }

        return serializer.toJson(rRes);
    }

    private Object logout(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

        String authToken = req.headers("Authorization");
        LogoutRequest rReq = new LogoutRequest(authToken);
        if(rReq.authToken() == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }

        String ret = service.logout(rReq);

        if(ret.equals("Yes")) {
            return serializer.toJson(new Object());
        } else {
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }
    }

    private Object listGames(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

        String authToken = req.headers("Authorization");
        LogoutRequest rReq = new LogoutRequest(authToken);
        if(rReq.authToken() == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }

        ListResponse lRes = service.listGames(rReq);

        if(lRes == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }
        ArrayList<GameDataShort> lis = new ArrayList<>();
        ListResponseShort newRes = new ListResponseShort(lis);
        for (int i = 0; i < lRes.games().size(); i++){
            newRes.games().add(new GameDataShort(lRes.games().get(i).gameID(), lRes.games().get(i).whiteUsername(), lRes.games().get(i).blackUsername(), lRes.games().get(i).gameName()));
        }

        return serializer.toJson(newRes);
    }

    private Object createGame(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

        StringRequest rReq = serializer.fromJson(req.body(), StringRequest.class);
        String name = rReq.gameName();
        String authToken = req.headers("Authorization");
        CreateGameRequest cReq = new CreateGameRequest(authToken, name);
        if(cReq.gameName() == null){
            res.status(400);
            return serializer.toJson(new ErrorMessage("Error: bad request"));
        }
        CreateGameResponse cRes = service.createGame(cReq);
        if(cRes == null){
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        }
        return serializer.toJson(cRes);
    }

    private Object joinGame(Request req, Response res) throws DataAccessException{
        Service service = new Service();
        var serializer = new Gson();

        StringIDRequest rReq = serializer.fromJson(req.body(), StringIDRequest.class);
        String authToken = req.headers("Authorization");
        JoinRequest jReq = new JoinRequest(rReq.playerColor(), rReq.gameID(), authToken);
        if(jReq.playerColor() == null || jReq.gameID() == null){
            res.status(400);
            return serializer.toJson(new ErrorMessage("Error: bad request"));
        }

        String check = service.joinGame(jReq);

        if(check.equals("no auth")) {
            res.status(401);
            return serializer.toJson(new ErrorMessage("Error: unauthorized"));
        } else if (check.equals("no game") || check.equals("bad color")) {
            res.status(400);
            return serializer.toJson(new ErrorMessage("Error: bad request"));
        } else if (check.equals("taken")) {
            res.status(403);
            return serializer.toJson(new ErrorMessage("Error: already taken"));
        } else {
            return serializer.toJson(new Object());
        }
    }

}
