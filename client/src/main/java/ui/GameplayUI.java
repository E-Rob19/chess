package ui;

import facade.ServerFacade;
import model.GameData;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class GameplayUI implements NotificationHandler {
    private static String command;
    private static String[] params;
    private String authToken;
    private String username;
    public ServerFacade server;
    public WebSocketFacade ws;
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
        this.ws = ws;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to the gameplay!\n");
        help();
        String[] lis = {};
        redraw(lis);
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
        System.out.print(" - highlight\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - help\n");

        //System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        //System.out.print(" - quit\n");
    }

    private void redraw(String[] params){
        System.out.print("imagine this is the chess board\n");
    }

    private void leave(String[] params){
        check = false;
    }

    private void move(String[] params){

    }

    private void resign(String[] params){

    }

    private void highlight(String[] params){

    }

    public void notify(ServerMessage notification) {
        System.out.println(notification.getMessage());
        System.out.println("testing notify 3\n");
        //printPrompt();
    }

}
