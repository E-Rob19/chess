package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDataAccess{
    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String authToken = rs.getString("authToken");
                        String un = rs.getString("username");
                        return new AuthData(authToken, un);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public ArrayList<AuthData> listAuths() throws DataAccessException {
        var result = new ArrayList<AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String authToken = rs.getString("authToken");
                        String un = rs.getString("username");
                        result.add(new AuthData(authToken, un));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public AuthData getAuthFromToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String aT = rs.getString("authToken");
                        String un = rs.getString("username");
                        return new AuthData(aT, un);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();
        execute(statement, authToken, username);
        return authToken;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        execute(statement, auth.authToken());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auths";
        execute(statement);
    }

    public static void execute(String statement, Object... things) throws  DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prep = conn.prepareStatement(statement)) {
                for (var i = 0; i < things.length; i++) {
                    var param = things[i];
                    if (param instanceof String p) {prep.setString(i + 1, p);}
                    else if (param instanceof Integer p) {prep.setInt(i + 1, p);}
                    else if (param instanceof ChessGame p) {prep.setString(i + 1, p.toString());}
                    else if (param == null) {prep.setNull(i + 1, NULL);}
                }
                prep.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
