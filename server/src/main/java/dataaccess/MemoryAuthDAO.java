package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDataAccess{

    private static final ArrayList<AuthData> Auths = new ArrayList<>();

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        for (AuthData auth : Auths) {
            if (auth.username().equals(username)) {
                return auth;
            }
        }
        return null;
    }

    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        for (AuthData auth : Auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> listAuths() throws DataAccessException{
        return Auths;
    }

    //public static String generateToken() {
    //    return UUID.randomUUID().toString();

    @Override
    public void createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString(); //generateToken();
        Auths.addFirst(new AuthData(authToken, username));

    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        Auths.remove(auth);
    }

    @Override
    public void clear() throws DataAccessException {
        Auths.clear();
    }
}
