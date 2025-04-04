package ui;

import facade.ServerFacade;
import model.GameData;
import websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class GameplayUI {
    private static String command;
    private static String[] params;
    private String authToken;
    private String username;
    public ServerFacade server;
    private ArrayList<GameData> gameList;
    private boolean check = true;
    private PrintChessBoard printFunc = new PrintChessBoard();

    public static void parseInput(String input){
        var token = input.toLowerCase().split(" ");
        command = (token.length > 0) ? token[0] : "help";
        params = Arrays.copyOfRange(token, 1, token.length);
    }

    public void eval(String authToken, ServerFacade server, String username, WebSocketFacade ws) throws DataFormatException {
        check = true;
        this.authToken = authToken;
        this.server = server;
        this.username = username;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to the gameplay!\n");
        help();
        String[] lis = {};
        listGames(lis);
        while (check) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print("[LOGGED-IN] >>> ");
            parseInput(scanner.nextLine());
            switch (command) {
                case "redraw" -> create(params);
                case "leave" -> listGames(params);
                case "move" -> play(params);
                case "resign" -> observe(params);
                case "highlight" -> logout(params);
                default -> help();
            }
        }
    }
}
