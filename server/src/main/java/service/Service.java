package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

public class Service {
    UserDataAccess userDatabase = new MemoryUserDAO();
    AuthDataAccess authDatabase = new MemoryAuthDAO();
    GameDataAccess gameDatabase = new MemoryGameDAO();

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
        if(user.password().equals(loginRequest.password())){
            authDatabase.createAuth(loginRequest.username());
            return new RegisterResult(loginRequest.username(), authDatabase.getAuth(loginRequest.username()).authToken());
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

    public void clear() throws DataAccessException {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }
}
