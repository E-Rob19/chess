package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class SQLAuthDAO implements AuthDataAccess{
    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<AuthData> listAuths() throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void createAuth(String username) throws DataAccessException {

    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
