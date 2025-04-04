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
                case "redraw" -> redraw(params);
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "highlight" -> highlight(params);
                default -> help();
            }
        }

        private void help(){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print("Available commands:\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - redraw <NAME>\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - leave\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - move <ID> [WHITE/BLACK]\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - resign <ID>\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - lighlight\n");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.print(" - help\n");

            //System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            //System.out.print(" - quit\n");
        }

    }
}
