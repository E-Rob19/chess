package ui;

//import dataaccess.DataAccessException;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import facade.ServerFacade;
import requests.*;
import model.GameData;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class PostLoginUI implements NotificationHandler {
    private static String command;
    private static String[] params;
    private String authToken;
    private String username;
    public ServerFacade server;
    public WebSocketFacade ws;
    private ArrayList<GameData> gameList;
    private boolean check = true;
    //private PrintChessBoard printFunc = new PrintChessBoard();
    private final NotificationHandler notificationHandler;
    boolean playingWhite;
    //private WebSocketFacade ws;

    public PostLoginUI() throws IOException {
        this.notificationHandler = this;
        ws = new WebSocketFacade("http://localhost:8080", this);
    }

    public static void parseInput(String input){
        var token = input.toLowerCase().split(" ");
        command = (token.length > 0) ? token[0] : "help";
        params = Arrays.copyOfRange(token, 1, token.length);
    }

    public void eval(String authToken, ServerFacade server, String username) throws DataFormatException, IOException, InvalidMoveException {
        check = true;
        this.authToken = authToken;
        this.server = server;
        this.username = username;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to Chess!\n");
        help();
        String[] lis = {};
        listGames(lis);
        while (check) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print("[LOGGED-IN] >>> ");
            parseInput(scanner.nextLine());
            switch (command) {
                case "create" -> create(params);
                case "list" -> listGames(params);
                case "play" -> play(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                default -> help();
            }
        }
    }

    private void help(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("Available commands:\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - create <NAME>\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - list\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - play <ID> [WHITE/BLACK]\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - observe <ID>\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - logout\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - help\n");

        //System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        //System.out.print(" - quit\n");
    }

    private void play(String[] params) throws DataFormatException, IOException, InvalidMoveException {
        if (params.length == 2) {
            int id = 0;
            try {
                id = Integer.parseInt(params[0]);
            } catch(Exception ex) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("not a valid game number\n");
                return;
            }
            String color = params[1];
            if(!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("not a valid color\n");
                return;
            }
            if(!(id > 0 && id <= gameList.size())){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("not a valid game number\n");
                return;
            }
            GameData game = gameList.get(id-1);
            boolean colorCheck = false;
            if(color.equalsIgnoreCase("white")){
                if(game.whiteUsername() != null) {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print("game already has a white player\n");
                    return;
                }
                if(Objects.equals(game.blackUsername(), username)){
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print("cannot play as both players\n");
                    return;
                }
            }
            if(color.equalsIgnoreCase("black")){
                colorCheck = true;
                if(game.blackUsername() != null) {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print("game already has a black player\n");
                    return;
                }
                if(Objects.equals(game.whiteUsername(), username)){
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print("cannot play as both players\n");
                    return;
                }
            }
            JoinRequest req = new JoinRequest(color, id, authToken);
            String res = server.joinGame(req);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("Successful join!\n");
            playingWhite = !colorCheck;
            ws.connect(authToken, id);
            new GameplayUI().eval(authToken, server, username, ws, gameList.get(id-1), colorCheck);
            return;
        }
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("unable to join\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("join with an id on the list of games and a color\n");
    }

    private void create(String[] params) throws DataFormatException {
        if(params.length != 1){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("game name can only be one word\n");
        }
        CreateGameRequest req = new CreateGameRequest(authToken, params[0]);
        CreateGameResponse res = server.createGame(req);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Game Created!\n");
        String[] lis = {};
        listGames(lis);
    }

    private void listGames(String[] params) throws DataFormatException {
        if(params.length != 0){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("list does not take parameters\n");
        }
        LogoutRequest req = new LogoutRequest(authToken);
        ListResponse res = server.listGames(req);
        if(res == null){
            System.out.print("cannot list games\n");
            return;
        }
        gameList = res.games();
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("Games:\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        for (int i = 0; i < gameList.size(); i++){
            System.out.print((i+1) + " : " + gameList.get(i).gameName());
            System.out.print(" W:" + gameList.get(i).whiteUsername());
            System.out.print(" B:" + gameList.get(i).blackUsername() + "\n");
        }
    }

    private void observe(String[] params) throws DataFormatException, IOException, InvalidMoveException {
        if(params.length == 1){
            int id = 0;
            try {
                id = Integer.parseInt(params[0]);
            } catch(Exception ex) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("not a number\n");
                return;
            }
            if(!(id > 0 && id <= gameList.size())){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("not a valid game number\n");
                return;
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("observe game " + id + "\n");
            //add print chess board things here
            //printFunc.print(null);
            ws.connect(authToken, id);
            new GameplayUI().eval(authToken, server, username, ws, gameList.get(id-1), false);
            return;
        }
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("observe only takes a game number\n");
    }

    private void logout(String[] params) throws DataFormatException {
        if(params.length != 0){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("logout does not take parameters\n");
        }
        LogoutRequest req = new LogoutRequest(authToken);
        String res = server.logout(req);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Logged out\n");
        check = false;

    }

    public void notify(ServerMessage notification, String message) {
        if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadGameMessage gameGame = new Gson().fromJson(message, LoadGameMessage.class);
            ChessGame game = new Gson().fromJson(gameGame.getMessage(), ChessGame.class);
            System.out.print("\n");
            PrintChessBoard printer = new PrintChessBoard();
            if(playingWhite) {
                printer.print(game, null);
            } else {
                printer.printBack(game, null);
            }
            return;
        }
        var tokens = message.toLowerCase().split("\"");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        System.out.println(tokens[3]);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println("\n[IN-GAME] >>> ");
    }
}
