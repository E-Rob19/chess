package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDataAccess {
    AuthData getAuth(String username) throws DataAccessException;

    ArrayList<AuthData> listAuths() throws DataAccessException;

    AuthData getAuthFromToken(String authToken) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    void deleteAuth(AuthData auth) throws DataAccessException;

    void clear() throws DataAccessException;

}
