package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
import service.RegisterRequest;
import service.RegisterResult;
import service.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    static UserDataAccess userDatabase;
    static AuthDataAccess authDatabase;
    static GameDataAccess gameDatabase;

    @AfterAll
    static void clearAll() throws DataAccessException {
        userDatabase.clear();
        authDatabase.clear();
        gameDatabase.clear();
    }

    @BeforeAll
    public static void init() throws DataAccessException {
        userDatabase = new SQLUserDAO();
        authDatabase = new SQLAuthDAO();
        gameDatabase = new SQLGameDAO();
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }


    //getAuth
    @Test
    void getAuthTestPositive() throws DataAccessException {
        String auth = authDatabase.createAuth("emma");
        AuthData expected = new AuthData(auth, "emma");
        AuthData actual = authDatabase.getAuth("emma");

        assertEquals(expected, actual);
        clearAll();
    }
    @Test
    void getAuthTestNegative() throws DataAccessException {
        //String auth = authDatabase.createAuth("emma");
        //AuthData expected = new AuthData(auth, "emma")
        AuthData actual = authDatabase.getAuth("emma");

        assertNull(actual);
        clearAll();
    }

    //listAuth
    @Test
    void listAuthTestPositive() throws DataAccessException {
        String auth1 = authDatabase.createAuth("emma");
        String auth2 = authDatabase.createAuth("leslie");
        AuthData authData1 = new AuthData(auth1, "emma");
        AuthData authData2 = new AuthData(auth2, "leslie");
        ArrayList<AuthData> expected = new ArrayList<AuthData>();
        expected.add(authData1);
        expected.add(authData2);
        ArrayList<AuthData> actual = authDatabase.listAuths();

        assertTrue(actual.containsAll(expected));
        clearAll();
    }
    @Test
    void listAuthTestNegative() throws DataAccessException {
        ArrayList<AuthData> expected = new ArrayList<AuthData>();
        ArrayList<AuthData> actual = authDatabase.listAuths();

        assertEquals(expected, actual);
        clearAll();
    }
    //createAuths
    @Test
    void createAuthTestPositive() throws DataAccessException {
        String auth = authDatabase.createAuth("leslie");
        AuthData expected = new AuthData(auth, "leslie");
        AuthData actual = authDatabase.getAuth("leslie");

        assertEquals(expected, actual);
        clearAll();
    }
    @Test
    void createAuthTestNegative() throws DataAccessException {
        //String auth = authDatabase.createAuth("lemma");
        //AuthData expected = new AuthData(auth, "lemma")
        AuthData actual = authDatabase.getAuth("lemma");

        assertNull(actual);
        clearAll();
    }
    //deleteAuth
    @Test
    void deleteAuthTestPositive() throws DataAccessException {
        String auth = authDatabase.createAuth("leslie");
        //AuthData expected = new AuthData(auth, "leslie");
        AuthData actual = authDatabase.getAuth("leslie");
        authDatabase.deleteAuth(actual);
        actual = authDatabase.getAuth("leslie");

        assertNull(actual);
        clearAll();
    }
    @Test
    void deleteAuthTestNegative() throws DataAccessException {
        authDatabase.createAuth("leslie");
        AuthData expected = new AuthData("randomAuthToken", "emma");
        AuthData actual = authDatabase.getAuth("leslie");
        authDatabase.deleteAuth(expected);
        actual = authDatabase.getAuth("leslie");

        assertNotNull(actual);
        clearAll();
    }
    //clear
    @Test
    void clearAuthsTest() throws DataAccessException {
        String auth = authDatabase.createAuth("leslie");
        String auth1 = authDatabase.createAuth("emma");
        AuthData expected = new AuthData(auth, "leslie");
        AuthData actual = authDatabase.getAuth("leslie");
        authDatabase.clear();
        ArrayList<AuthData> auths = authDatabase.listAuths();
        ArrayList<AuthData> empty = new ArrayList<>();

        assertEquals(empty, auths);
        clearAll();
    }

    //getUser
    @Test
    void getUserTestPositive() throws DataAccessException {
        UserData expected = new UserData("leslie", "password", "email");
        userDatabase.createUser(new UserData("leslie", "password", "email"));
        UserData actual = userDatabase.getUser("leslie");

        assertTrue(BCrypt.checkpw(expected.password(), actual.password()));
        clearAll();
    }
    @Test
    void getUserTestNegative() throws DataAccessException {
        UserData actual = userDatabase.getUser("emma");

        assertNull(actual);
        clearAll();
    }
    //listUser
    @Test
    void listUserTestPositive() throws DataAccessException {
        UserData user = new UserData("emma", "password", "email");
        UserData user1 = new UserData("emma", "password", "email");
        userDatabase.createUser(new UserData("emma", "password", "email"));
        userDatabase.createUser(new UserData("leslie", "password", "email"));
        ArrayList<UserData> expected = new ArrayList<>();
        expected.add(user);
        expected.add(user1);
        ArrayList<UserData> actual = userDatabase.listUsers();

        assertEquals(expected.size(), actual.size());
        clearAll();
    }
    @Test
    void listUserTestNegative() throws DataAccessException {
        ArrayList<UserData> expected = new ArrayList<UserData>();
        ArrayList<UserData> actual = userDatabase.listUsers();

        assertEquals(expected, actual);
        clearAll();
    }
    //createUser
    @Test
    void createUserTestPositive() throws DataAccessException {
        UserData expected = new UserData("emma", "password", "email");
        userDatabase.createUser(new UserData("emma", "password", "email"));
        UserData actual = userDatabase.getUser("emma");

        assertTrue(BCrypt.checkpw(expected.password(), actual.password()));
        clearAll();
    }
    @Test
    void createUserTestNegative() throws DataAccessException {
        userDatabase.createUser(new UserData("emma", "password", "email"));
        assertThrows(DataAccessException.class, () -> userDatabase.createUser(new UserData("emma", "password", "email")));
        clearAll();
    }
    //clear
    @Test
    void clearUsersTest() throws DataAccessException {
        userDatabase.createUser(new UserData("emma", "password", "email"));
        userDatabase.createUser(new UserData("leslie", "password", "email"));
        ArrayList<UserData> expected = new ArrayList<UserData>();
        userDatabase.clear();
        ArrayList<UserData> actual = userDatabase.listUsers();

        assertEquals(expected, actual);
        clearAll();
    }

    //getGame
    @Test
    void getGameTestPositive() throws DataAccessException {
        gameDatabase.createGame("name");
        GameData actual = gameDatabase.getGame(1);
        GameData expected = new GameData(1, null, null, "name", actual.game());

        assertEquals(expected, actual);
        clearAll();
    }
    @Test
    void getGameTestNegative() throws DataAccessException {
        GameData actual = gameDatabase.getGame(1);

        assertNull(actual);
        clearAll();
    }
    //createGame
    @Test
    void createGameTestPositive() throws DataAccessException {
        gameDatabase.createGame("emma");
        GameData actual = gameDatabase.getGame(1);
        GameData expected = new GameData(1, null, null, "emma", actual.game());

        assertEquals(expected, actual);
        clearAll();
    }
    @Test
    void createGameTestNegative() throws DataAccessException {
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");

        assertEquals(3, gameDatabase.listGames().size());
        clearAll();
    }
    //listGame
    @Test
    void listGameTestPositive() throws DataAccessException {
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");
        GameData game1 = new GameData(1, null, null, "name", gameDatabase.getGame(1).game());
        GameData game2 = new GameData(2, null, null, "name", gameDatabase.getGame(2).game());
        GameData game3 = new GameData(3, null, null, "name", gameDatabase.getGame(3).game());
        ArrayList<GameData> actual = gameDatabase.listGames();
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(game1);
        expected.add(game2);
        expected.add(game3);

        assertEquals(expected.size(), actual.size());
        clearAll();

    }
    @Test
    void listGameTestNegative() throws DataAccessException {
        ArrayList<GameData> actual = gameDatabase.listGames();
        ArrayList<GameData> expected = new ArrayList<>();

        assertEquals(expected, actual);
        clearAll();
    }
    //addPlayer
    @Test
    void addPlayerTestPositive() throws DataAccessException {
        userDatabase.createUser(new UserData("emma", "password", "email"));
        gameDatabase.createGame("name");
        gameDatabase.addPlayer(1, "emma", "WHITE");
        String playerName = gameDatabase.getGame(1).whiteUsername();

        assertEquals("emma", playerName);
        clearAll();
    }
    @Test
    void addPlayerTestNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDatabase.addPlayer(1, "leslie", ""));
    }
    //clear
    @Test
    void clearGamesTest() throws DataAccessException {
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");
        gameDatabase.createGame("name");
        gameDatabase.clear();
        ArrayList<GameData> actual = gameDatabase.listGames();
        ArrayList<GameData> expected = new ArrayList<>();

        assertEquals(expected, actual);
        clearAll();
    }
}
