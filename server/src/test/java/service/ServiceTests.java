package service;

import chess.ChessGame;
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
        //AuthDataAccess authDatabase = service.authDatabase
        //UserDataAccess userDatabase = service.userDatabase
        //GameDataAccess gameDatabase = service.gameDatabase
        assertEquals(userDatabase, userList);
        assertEquals(authDatabase, authList);
        assertEquals(gameDatabase, gameList);
        service.clear();
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
        service.clear();
    }

    @Test
    void loginNegative() throws DataAccessException {
        Service service = new Service();
        LoginRequest req = new LoginRequest("leslie", "emma");

        RegisterResult actual = service.login(req);

        assertNull(actual);
        service.clear();
    }

    //logout
    @Test
    void logoutPositive() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        service.login(new LoginRequest(AUser.username(), AUser.password()));
        String check = service.logout(new LogoutRequest(service.authDatabase.getAuth(AUser.username()).authToken()));
        assertEquals("Yes", check);
        RegisterResult res = service.login(new LoginRequest(AUser.username(), AUser.password()));
        assertNotNull(res);
        service.clear();
    }

    @Test
    void logoutNegative() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        String check = service.logout(new LogoutRequest("service.authDatabase.getAuth(AUser.username()).authToken())"));
        assertEquals("no", check);
        service.clear();
    }


    //list Games
    @Test
    void listPositive() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        service.createGame(new CreateGameRequest(service.authDatabase.getAuth(AUser.username()).authToken(), "name"));
        ListResponse games = service.listGames(new LogoutRequest(service.authDatabase.getAuth(AUser.username()).authToken()));
        ArrayList<GameDataShort> expected = new ArrayList<GameDataShort>();
        expected.add(new GameDataShort(1, null, null, "name"));
        assertEquals(expected, games.games());
        service.clear();
    }

    @Test
    void listNegative() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));

        ListResponse games = service.listGames(new LogoutRequest(service.authDatabase.getAuth(AUser.username()).authToken()));
        assertEquals(new ArrayList<ChessGame>(), games.games());
        service.clear();
    }

    //create Games
    @Test
    void createGamePositive() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        service.createGame(new CreateGameRequest(service.authDatabase.getAuth(AUser.username()).authToken(), "name"));
        assertNotNull(service.gameDatabase.getGame(1));
        service.clear();
    }

    @Test
    void createGameNegative() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        CreateGameResponse res = service.createGame(new CreateGameRequest("service.authDatabase.getAuth(AUser.username()).authToken()", "name"));
        assertNull(res);
        service.clear();
    }

    //join game
    @Test
    void joinPositive() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        service.createGame(new CreateGameRequest(service.authDatabase.getAuth(AUser.username()).authToken(), "name"));
        service.joinGame(new JoinRequest("WHITE", 1, service.authDatabase.getAuth("Emma").authToken()));
        assertEquals("Emma", service.gameDatabase.getGame(1).whiteUsername());
        service.clear();
    }

    @Test
    void joinNegative() throws DataAccessException {
        Service service = new Service();
        UserData AUser = new UserData("Emma", "leslie", "email");
        service.register(new RegisterRequest(AUser.username(), AUser.password(), AUser.email()));
        service.createGame(new CreateGameRequest(service.authDatabase.getAuth(AUser.username()).authToken(), "name"));
        service.joinGame(new JoinRequest("WHITE", 1, "service.authDatabase.getAuth().authToken()"));
        assertNull(service.gameDatabase.getGame(1).whiteUsername());
        service.clear();
    }

}