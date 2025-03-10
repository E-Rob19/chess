package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDataAccess{
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String un = rs.getString("username");
                        String email = rs.getString("email");
                        return new UserData(un, password, email);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public ArrayList<UserData> listUsers() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String password = rs.getString("password");
                        String un = rs.getString("username");
                        String email = rs.getString("email");
                        result.add(new UserData(un, password, email));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        execute(statement, user.username(), user.password(), user.email());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        execute(statement);
    }

    private void execute(String statement, Object... things) throws  DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prep = conn.prepareStatement(statement)) {
                for (var i = 0; i < things.length; i++) {
                    var param = things[i];
                    if (param instanceof String p) prep.setString(i + 1, p);
                    else if (param instanceof Integer p) prep.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) prep.setString(i + 1, p.toString());
                    else if (param == null) prep.setNull(i + 1, NULL);
                }
                prep.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
