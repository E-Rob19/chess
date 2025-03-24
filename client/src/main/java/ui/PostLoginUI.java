package ui;

import Facade.ServerFacade;
import RequestsAndResponses.*;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class PostLoginUI {
    private static String command;
    private static String[] params;
    private String authToken;
    public ServerFacade server;
    private ArrayList<GameDataShort> gameList;
    private boolean check = true;

    public static void parseInput(String input){
        var tokens = input.toLowerCase().split(" ");
        command = (tokens.length > 0) ? tokens[0] : "help";
        params = Arrays.copyOfRange(tokens, 1, tokens.length);
    }

    public void eval(String authToken, ServerFacade server) throws DataFormatException {
        check = true;
        this.authToken = authToken;
        this.server = server;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to Chess!\n");
        help();
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

    private void play(String[] params) throws DataFormatException {
        if (params.length == 2) {
            String id = params[0];
            String color = params[1];
            JoinRequest req = new JoinRequest(color, Integer.parseInt(id), authToken);
            String res = server.joinGame(req);
            if(res != null) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.print("Successful join?\n");
                return;
            }
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

    private void observe(String[] params){

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
}
