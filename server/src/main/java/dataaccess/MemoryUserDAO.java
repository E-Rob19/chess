package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDataAccess {

    private static final ArrayList<UserData> USERS = new ArrayList<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : USERS) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        return USERS;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        USERS.add(user);
    }

    public void clear(){
        USERS.clear();
    }
}
