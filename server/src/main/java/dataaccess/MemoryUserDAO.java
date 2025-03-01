package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDataAccess {

    private static final ArrayList<UserData> users = new ArrayList<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        return users;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.add(user);
    }

    public void clear(){
        users.clear();
    }
}
