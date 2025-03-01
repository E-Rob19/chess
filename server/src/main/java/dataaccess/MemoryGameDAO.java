package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDataAccess{

    private static final ArrayList<GameData> games = new ArrayList<>();
    int gameNum = 0;

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(String gameName) throws DataAccessException {

    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void addPlayer(String gameID, String username, String playerColor) {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
