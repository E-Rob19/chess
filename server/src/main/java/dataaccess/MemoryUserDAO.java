package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDataAccess {

    private static final ArrayList<UserData> Users = new ArrayList<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : Users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        return Users;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        Users.add(user);
    }

    public void clear(){
        Users.clear();
    }
}
