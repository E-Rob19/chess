package dataaccess;

import model.UserData;

public interface UserDataAccess {
    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void clear() throws DataAccessException;
}

