package dataaccess;

import model.UserData;

import java.util.ArrayList;

public interface UserDataAccess {
    UserData getUser(String username) throws DataAccessException;

    ArrayList<UserData> listUsers() throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void clear() throws DataAccessException;
}

