package ui;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class PregameUI {
    private static String command;
    private static String[] params;

//    public static void main(String[] args) throws Exception {
//        pregameUI();
//    }

    private void parseInput(String input){
        var tokens = input.toLowerCase().split(" ");
        command = (tokens.length > 0) ? tokens[0] : "help";
        params = Arrays.copyOfRange(tokens, 1, tokens.length);
    }

    public void eval(){
        Scanner scanner = new Scanner(System.in);
        while (!Objects.equals(command, "quit")) {
            System.out.printf("Type your numbers%n>>> ");
            //try {
            parseInput(scanner.nextLine());
            switch (command) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> {return;}
                default -> help();
            };
            //} catch (DataFormatException ex) {
            //    return ex.getMessage();
            //}
        }
    }

    private void help(){

    }

    private void login(String[] params){

    }

    private void register(String[] params){

    }

}
