package service;

import dataaccess.*;
import model.UserData;

public class Service {
    UserDataAccess userDatabase = new MemoryUserDAO();
    AuthDataAccess authDatabase = new MemoryAuthDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if(userDatabase.getUser(registerRequest.username())==null){
            userDatabase.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            authDatabase.createAuth(registerRequest.username());
            return new RegisterResult(registerRequest.username(), authDatabase.getAuth(registerRequest.username()).authToken());
        }
        return null;
    }
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
    public void clear(){

    }
}
