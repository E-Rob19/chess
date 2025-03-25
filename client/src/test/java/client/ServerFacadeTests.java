package client;

import Facade.ServerFacade;
import RequestsAndResponses.*;
import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.Service;
import ui.Client;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void clear() throws DataFormatException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    //register
    @Test
    void registerPositive() throws DataAccessException, DataFormatException {
        Service service = new Service();
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");

        RegisterResult actual = facade.register(req);

        assertNotNull(actual);
    }

    @Test
    void registerNegative() throws DataAccessException, DataFormatException {
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        facade.register(req);
        RegisterResult actual = facade.register(req);

        assertNull(actual);
    }

    //clear
    @Test
    void clearPositive() throws DataAccessException, DataFormatException {
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        facade.register(req);

        facade.clear();

        assertNull(facade.login(new LoginRequest("emma", "leslie")));
    }

    //login
    @Test
    void loginPositive() throws DataAccessException, DataFormatException {
        RegisterRequest req = new RegisterRequest("emma", "leslie", "email");
        facade.register(req);
        LoginRequest lReq = new LoginRequest("emma", "leslie");

        RegisterResult actual = facade.login(lReq);

        assertNotNull(actual);
    }

    @Test
    void loginNegative() throws DataAccessException, DataFormatException {
        LoginRequest req = new LoginRequest("leslie", "emma");

        RegisterResult actual = facade.login(req);

        assertNull(actual);
    }

    //logout
    @Test
    void logoutPositive() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        RegisterResult res = facade.login(new LoginRequest(aUser.username(), aUser.password()));
        String check = facade.logout(new LogoutRequest(res.authToken()));
        assertNull(check);
    }

    @Test
    void logoutNegative() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        String check = facade.logout(new LogoutRequest("service.authDatabase.getAuth(aUser.username()).authToken())"));
        assertNull(check);
    }


    //list Games
    @Test
    void listPositive() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        RegisterResult res =  facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        facade.createGame(new CreateGameRequest(res.authToken(), "name"));
        ListResponse games = facade.listGames(new LogoutRequest(res.authToken()));
        ArrayList<GameData> expected = new ArrayList<GameData>();
        expected.add(new GameData(1, null, null, "name", null));
        assertEquals(expected, games.games());
    }

    @Test
    void listNegative() throws DataAccessException {
        Service service = new Service();
        UserData aUser = new UserData("Emma", "leslie", "email");
        RegisterResult res = service.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));

        ListResponse games = service.listGames(new LogoutRequest(res.authToken()));
        assertEquals(new ArrayList<GameData>(), games.games());
        service.clear();
    }

    //create Games
    @Test
    void createGamePositive() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        RegisterResult res = facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        CreateGameResponse rep =  facade.createGame(new CreateGameRequest(res.authToken(), "name"));
        assertNotNull(rep);
    }

    @Test
    void createGameNegative() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        CreateGameResponse res = facade.createGame(new CreateGameRequest("service.authDatabase.getAuth(aUser.username()).authToken()", null));
        assertNull(res);
    }

    //join game
    @Test
    void joinPositive() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        RegisterResult res = facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        CreateGameResponse rep = facade.createGame(new CreateGameRequest(res.authToken(), "name"));
        String res1 = facade.joinGame(new JoinRequest("WHITE", 1, res.authToken()));
        assertNotNull(rep);
        assertNull(res1);
    }

    @Test
    void joinNegative() throws DataAccessException, DataFormatException {
        UserData aUser = new UserData("Emma", "leslie", "email");
        RegisterResult res = facade.register(new RegisterRequest(aUser.username(), aUser.password(), aUser.email()));
        CreateGameResponse rep = facade.createGame(new CreateGameRequest(res.authToken(), "name"));
        String check = facade.joinGame(new JoinRequest("WHITE", 1, "service.authDatabase.getAuth().authToken()"));
        assertNull(check);
    }

}
