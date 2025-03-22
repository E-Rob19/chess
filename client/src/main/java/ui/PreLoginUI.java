package ui;

import dataaccess.DataAccessException;
import service.RegisterRequest;

import Facade.ServerFacade;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class PreLoginUI {
    private static String command;
    private static String[] params;
    private static ServerFacade server = new ServerFacade("http://localhost:8080");


    private void parseInput(String input){
        var tokens = input.toLowerCase().split(" ");
        command = (tokens.length > 0) ? tokens[0] : "help";
        params = Arrays.copyOfRange(tokens, 1, tokens.length);
    }

    public void eval() throws DataFormatException, DataAccessException {
        Scanner scanner = new Scanner(System.in);
        while (!Objects.equals(command, "quit")) {
            System.out.printf(">>> ");
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
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - register <USERNAME> <PASSWORD> <EMAIL>\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - login <USERNAME> <PASSWORD>\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - help\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - quit\n");
    }

    private void login(String[] params){
        System.out.print("LOGIN\n");
    }

    private void register(String[] params) throws DataFormatException, DataAccessException {
        if (params.length >= 1) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest req = new RegisterRequest(username, password, email);
            server.register(req);
            return;
        }
        System.out.print("wrong number of inputs\n");
        System.out.print("register with a username, password, and email\n");
    }

}
