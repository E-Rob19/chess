package dataaccess;

import model.GameData;
import service.GameDataShort;

import java.util.ArrayList;

public class SQLGameDAO implements GameDataAccess{
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameDataShort> listGamesForResponse() throws DataAccessException {
        return null;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
