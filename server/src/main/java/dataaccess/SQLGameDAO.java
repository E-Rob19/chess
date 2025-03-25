package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import requests.GameDataShort;

import java.util.ArrayList;

public class SQLGameDAO implements GameDataAccess {

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
        ChessGame newGame = new ChessGame();
        SQLAuthDAO.execute(statement, null, null, gameName, new Gson().toJson(newGame));
        gameNum = gameNum + 1;
        return gameNum - 1;
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
        if (playerColor.equalsIgnoreCase("WHITE")) {
            statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?;";
        } else if (playerColor.equalsIgnoreCase("BLACK")) {
            statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?;";
        }
        SQLAuthDAO.execute(statement, username, gameID);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        gameNum = 1;
        SQLAuthDAO.execute(statement);
    }
}
