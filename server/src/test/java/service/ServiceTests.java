package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {


    //register
    @Test
    void registerPositive() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");

        RegisterResult actual = service.register(req);

        assertNotNull(actual);
    }

    @Test
    void registerNegative() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        service.register(req);
        RegisterResult actual = service.register(req);

        assertNull(actual);
    }

    //clear
    @Test
    void clearPositive() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        service.register(req);

        service.clear();

        ArrayList<UserData> userList = new ArrayList<>();
        ArrayList<AuthData> authList = new ArrayList<>();
        ArrayList<GameData> gameList = new ArrayList<>();
        ArrayList<AuthData> authDatabase = service.authDatabase.listAuths();
        ArrayList<UserData> userDatabase = service.userDatabase.listUsers();
        ArrayList<GameData> gameDatabase = service.gameDatabase.listGames();
        //AuthDataAccess authDatabase = service.authDatabase;
        //UserDataAccess userDatabase = service.userDatabase;
        //GameDataAccess gameDatabase = service.gameDatabase;
        assertEquals(userDatabase, userList);
        assertEquals(authDatabase, authList);
        assertEquals(gameDatabase, gameList);
    }

    //login
    @Test
    void loginPositive() throws DataAccessException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        service.register(req);
        LoginRequest lReq = new LoginRequest("emma", "leslie");

        RegisterResult actual = service.login(lReq);

        assertNotNull(actual);
    }

    @Test
    void loginNegative() throws DataAccessException {
        Service service = new Service();
        LoginRequest req = new LoginRequest("emma", "leslie");

        RegisterResult actual = service.login(req);

        assertNull(actual);
    }

    //logout
    @Test
    void logoutPositive() throws DataAccessException {
        Service service = new Service();

    }

    @Test
    void logoutNegative() throws DataAccessException {
        Service service = new Service();

    }


    //list Games
    @Test
    void listPositive() throws DataAccessException {
        Service service = new Service();

    }

    @Test
    void listNegative() throws DataAccessException {
        Service service = new Service();

    }

    //create Games
    @Test
    void createGamePositive() throws DataAccessException {
        Service service = new Service();

    }

    @Test
    void createGameNegative() throws DataAccessException {
        Service service = new Service();

    }

    //join game
    @Test
    void joinPositive() throws DataAccessException {
        Service service = new Service();

    }

    @Test
    void joinNegative() throws DataAccessException {
        Service service = new Service();

    }

}