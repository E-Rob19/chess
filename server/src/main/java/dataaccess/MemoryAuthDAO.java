package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDataAccess{

    private static final ArrayList<AuthData> auths = new ArrayList<>();

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.username().equals(username)) {
                return auth;
            }
        }
        return null;
    }

    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> listAuths() throws DataAccessException{
        return auths;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void createAuth(String username) throws DataAccessException {
        String authToken = generateToken();
        auths.add(new AuthData(authToken, username));
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        auths.remove(auth);
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
