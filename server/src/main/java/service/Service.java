package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Types.NULL;

public class Service {

    UserDataAccess userDatabase;
    AuthDataAccess authDatabase;
    GameDataAccess gameDatabase;

    public Service() throws DataAccessException {
        userDatabase = new SQLUserDAO();
        authDatabase = new SQLAuthDAO();
        gameDatabase = new SQLGameDAO();
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if(userDatabase.getUser(registerRequest.username())==null){
            userDatabase.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            authDatabase.createAuth(registerRequest.username());
            return new RegisterResult(registerRequest.username(), authDatabase.getAuth(registerRequest.username()).authToken());
        }
        return null;
    }

    public RegisterResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData user = userDatabase.getUser(loginRequest.username());
        if(user == null){
            return null;
        }
        if(user.password().equals(loginRequest.password()) || BCrypt.checkpw(loginRequest.password(), user.password())){
            String authToken = authDatabase.createAuth(loginRequest.username());
            return new RegisterResult(loginRequest.username(), authToken);
        }
        return null;
    }

    public String logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData auth = authDatabase.getAuthFromToken(logoutRequest.authToken());
        if (auth == null){
            return "no";
        }
        authDatabase.deleteAuth(auth);
        return "Yes";
    }

    public ListResponse listGames(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData auth = authDatabase.getAuthFromToken(logoutRequest.authToken());
        if (auth == null){
            return null;
        }

        return new ListResponse(gameDatabase.listGamesForResponse());
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData auth = authDatabase.getAuthFromToken(createGameRequest.authToken());
        if (auth == null){
            return null;
        }

        int gameID = gameDatabase.createGame(createGameRequest.gameName());

        return new CreateGameResponse(gameID);
    }

    public String joinGame(JoinRequest joinRequest) throws DataAccessException {
        AuthData auth = authDatabase.getAuthFromToken(joinRequest.authToken());
        if (auth == null){
            return "no auth";
        }
        GameData game = gameDatabase.getGame(joinRequest.gameID());
        if (game == null){
            return "no game";
        }
        if(joinRequest.playerColor().equals("WHITE")){
            if(game.whiteUsername() == null || game.whiteUsername().equals(auth.username())){
                gameDatabase.addPlayer(game.gameID(), auth.username(), joinRequest.playerColor());
            } else {
                return "taken";
            }
        } else if (joinRequest.playerColor().equals("BLACK")) {
            if(game.blackUsername() == null || game.blackUsername().equals(auth.username())){
                gameDatabase.addPlayer(game.gameID(), auth.username(), joinRequest.playerColor());
            } else {
                return "taken";
            }
        } else {
            return "bad color";
        }


        return "Yes";
    }

    public void clear() throws DataAccessException {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }



}
