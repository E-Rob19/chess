package dataaccess;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
import service.RegisterRequest;
import service.RegisterResult;
import service.Service;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    static UserDataAccess userDatabase;
    static AuthDataAccess authDatabase;
    static GameDataAccess gameDatabase;

    @AfterAll
    static void stopServer() throws DataAccessException {
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
    }
    @Test
    void getAuthTestNegative() throws DataAccessException {
        //String auth = authDatabase.createAuth("emma");
        //AuthData expected = new AuthData(auth, "emma")
        AuthData actual = authDatabase.getAuth("emma");

        assertNull(actual);
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
    }
    @Test
    void listAuthTestNegative() throws DataAccessException {
        //String auth1 = authDatabase.createAuth("emma");
        //String auth2 = authDatabase.createAuth("leslie");
        //AuthData authData1 = new AuthData(auth1, "emma");
        //AuthData authData2 = new AuthData(auth2, "leslie")
        ArrayList<AuthData> expected = new ArrayList<AuthData>();
        //expected.add(authData1);
        //expected.add(authData2)
        ArrayList<AuthData> actual = authDatabase.listAuths();

        assertEquals(expected, actual);
    }
    //createAuths
    @Test
    void createAuthTestPositive() throws DataAccessException {
        String auth = authDatabase.createAuth("leslie");
        AuthData expected = new AuthData(auth, "leslie");
        AuthData actual = authDatabase.getAuth("leslie");

        assertEquals(expected, actual);
    }
    @Test
    void createAuthTestNegative() throws DataAccessException {
        String auth = authDatabase.createAuth("lemma");
        AuthData expected = new AuthData(auth, "lemma");
        AuthData actual = authDatabase.getAuth("lemma");

        assertEquals(expected, actual);
    }
    //deleteAuth
    @Test
    void deleteAuthTestPositive() throws DataAccessException {

    }
    @Test
    void deleteAuthTestNegative() throws DataAccessException {

    }
    //clear
    @Test
    void clearAuthsTest() throws DataAccessException {

    }

    //getUser
    @Test
    void getUserTestPositive() throws DataAccessException {

    }
    @Test
    void getUserTestNegative() throws DataAccessException {

    }
    //listUser
    @Test
    void listUserTestPositive() throws DataAccessException {

    }
    @Test
    void listUserTestNegative() throws DataAccessException {

    }
    //createUser
    @Test
    void createUserTestPositive() throws DataAccessException {

    }
    @Test
    void createUserTestNegative() throws DataAccessException {

    }
    //clear
    @Test
    void clearUsersTest() throws DataAccessException {

    }

    //getGame
    @Test
    void getGameTestPositive() throws DataAccessException {

    }
    @Test
    void getGameTestNegative() throws DataAccessException {

    }
    //createGame
    @Test
    void createGameTestPositive() throws DataAccessException {

    }
    @Test
    void createGameTestNegative() throws DataAccessException {

    }
    //listGame
    @Test
    void listGameTestPositive() throws DataAccessException {

    }
    @Test
    void listGameTestNegative() throws DataAccessException {

    }
    //addPlayer
    @Test
    void addPlayerTestPositive() throws DataAccessException {

    }
    @Test
    void addPlayerTestNegative() throws DataAccessException {

    }
    //clear
    @Test
    void clearGamesTest() throws DataAccessException {

    }
}
