package service;

import dataaccess.*;
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

    //public void logout(LogoutRequest logoutRequest) {}

    public void clear() throws DataAccessException {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }
}
