package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import service.GameDataShort;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDataAccess{

    static int gameNum = 1;

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String chessGame = rs.getString("chessGame");
                        ChessGame game = new Gson().fromJson(chessGame, ChessGame.class);
                        return new GameData(id, whiteUsername, blackUsername, gameName, game);
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
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
        execute(statement,  null, null, gameName, new ChessGame());
        gameNum = gameNum + 1;
        return gameNum-1;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String chessGame = rs.getString("chessGame");
                        ChessGame game = new Gson().fromJson(chessGame, ChessGame.class);
                        result.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public ArrayList<GameDataShort> listGamesForResponse() throws DataAccessException {
        var result = new ArrayList<GameDataShort>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        result.add(new GameDataShort(id, whiteUsername, blackUsername, gameName));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) throws DataAccessException {
        String statement = "";
        if(playerColor.equals("WHITE")){
            statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?;";
        } else if (playerColor.equals("BLACK")) {
            statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?;";
        }
        execute(statement, username, gameID);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        gameNum = 1;
        execute(statement);
    }

    private void execute(String statement, Object... things) throws  DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prep = conn.prepareStatement(statement)) {
                for (var i = 0; i < things.length; i++) {
                    var param = things[i];
                    if (param instanceof String p) prep.setString(i + 1, p);
                    else if (param instanceof Integer p) prep.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) prep.setString(i + 1, new Gson().toJson(p));
                    else if (param == null) prep.setNull(i + 1, NULL);
                }
                prep.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
