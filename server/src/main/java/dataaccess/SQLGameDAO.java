package dataaccess;

import model.GameData;
import service.GameDataShort;

import java.util.ArrayList;

public class SQLGameDAO implements GameDataAccess{
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
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
