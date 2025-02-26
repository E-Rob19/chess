package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDataAccess {

    private ArrayList<UserData> users = new ArrayList<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username() == username){
                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.add(user);
    }

    public void clear(){
        users.clear();
    }
}
