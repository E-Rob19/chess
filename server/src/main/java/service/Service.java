package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import model.UserData;

public class Service {
    UserDataAccess userDatabase = new MemoryUserDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if(userDatabase.getUser(registerRequest.username())==null){
            userDatabase.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        }
        return null;
    }
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}
