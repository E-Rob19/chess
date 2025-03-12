package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDataAccess{

    private static final ArrayList<AuthData> AUTHS = new ArrayList<>();

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        for (AuthData auth : AUTHS) {
            if (auth.username().equals(username)) {
                return auth;
            }
        }
        return null;
    }

    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        for (AuthData auth : AUTHS) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> listAuths() throws DataAccessException{
        return AUTHS;
    }

    //public static String generateToken() {
    //    return UUID.randomUUID().toString();

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString(); //generateToken();
        AUTHS.addFirst(new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        AUTHS.remove(auth);
    }

    @Override
    public void clear() throws DataAccessException {
        AUTHS.clear();
    }

}
