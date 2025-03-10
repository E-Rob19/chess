package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class SQLUserDAO implements UserDataAccess{
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
