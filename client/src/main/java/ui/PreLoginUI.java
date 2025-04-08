package ui;

import dataaccess.DataAccessException;
import requests.LoginRequest;
import requests.RegisterRequest;

import facade.ServerFacade;
import requests.RegisterResult;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class PreLoginUI implements NotificationHandler{
    private static String command;
    private static String[] params;
    public static ServerFacade server = new ServerFacade("http://localhost:8080");
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    public PreLoginUI() throws DataAccessException {
        this.notificationHandler = this;
        ws = new WebSocketFacade("http://localhost:8080", this);
    }


    public static void parseInput(String input){
        var tokens = input.toLowerCase().split(" ");
        command = (tokens.length > 0) ? tokens[0] : "help";
        ServerFacade test = server;
        params = Arrays.copyOfRange(tokens, 1, tokens.length);
    }

    public void eval() throws DataFormatException, DataAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to Chess!\n");
        help();
        while (!Objects.equals(command, "quit")) {
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(">>> ");
            parseInput(scanner.nextLine());
            switch (command) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> {return;}
                default -> help();
            }
        }
    }

    private void help(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("Available commands:\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - register <USERNAME> <PASSWORD> <EMAIL>\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - login <USERNAME> <PASSWORD>\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - help\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - quit\n");
    }

    private void login(String[] params) throws DataFormatException, DataAccessException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginRequest req = new LoginRequest(username, password);
            RegisterResult res = server.login(req);
            if(res != null) {
                String authToken = res.authToken();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.print("Successful login!\n");
                new PostLoginUI().eval(authToken, server, username, ws);
                return;
            }
        }
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("unable to login\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("login with an existing username and password, or register new user\n");
    }

    private void register(String[] params) throws DataFormatException, DataAccessException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest req = new RegisterRequest(username, password, email);
            RegisterResult res = server.register(req);
            if(res != null) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                System.out.print("Successfully registered!\n");
                login(Arrays.copyOf(params, params.length-1));
                return;
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.print("unable to register\n");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                System.out.print("register with an different username\n");
                return;
            }
        }
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("unable to register\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("register with a username, password, and email\n");
    }

    public void notify(ServerMessage notification) {
        //System.out.println(notification.getMessage());
        System.out.println("testing notify\n");
        //printPrompt();
    }

}
