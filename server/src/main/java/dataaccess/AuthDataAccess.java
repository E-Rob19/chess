package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

public interface AuthDataAccess {
    AuthData getAuth(String username) throws DataAccessException;

    ArrayList<AuthData> listAuths() throws DataAccessException;

    public AuthData getAuthFromToken(String authToken) throws DataAccessException;

    void createAuth(String username) throws DataAccessException;

    void deleteAuth(AuthData auth) throws DataAccessException;

    void clear() throws DataAccessException;

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
