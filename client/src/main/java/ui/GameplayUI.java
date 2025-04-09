package ui;

import chess.*;
import facade.ServerFacade;
import model.GameData;
import requests.ListResponse;
import requests.LogoutRequest;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class GameplayUI implements NotificationHandler {
    private static String command;
    private static String[] params;
    private String authToken;
    private String username;
    public ServerFacade server;
    public WebSocketFacade ws;
    private GameData gameData;
    boolean checkIfBlack;
    private boolean check = true;
    private PrintChessBoard printFunc = new PrintChessBoard();

    public static void parseInput(String input){
        var token = input.toLowerCase().split(" ");
        params = Arrays.copyOfRange(token, 1, token.length);
        command = (token.length > 0) ? token[0] : "help";
    }

    public void eval(String authToken, ServerFacade server, String username, WebSocketFacade ws, GameData gameData, boolean checkIfBlack) throws DataFormatException, IOException, InvalidMoveException {
        this.username = username;
        this.ws = ws;
        this.authToken = authToken;
        this.server = server;
        this.gameData = gameData;
        updateGame();
        this.checkIfBlack = checkIfBlack;
        check = true;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to the gameplay!\n");
        //String[] lis = {};
        //redraw(lis);
        help();
        while (check) {
            updateGame();
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print("[IN-GAME] >>> ");
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
        System.out.print(" - redraw\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - leave\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - move <start position> <end position>\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - resign\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - highlight <position>\n");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - help\n");

        //System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        //System.out.print(" - quit\n");
    }

    private void redraw(String[] params) throws DataFormatException {
        if(params.length > 0){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("redraw doesn't take any inputs\n");
            return;
        }
        updateGame();
        if(checkIfBlack){
            printFunc.printBack(gameData.game(), null);
        } else {
            printFunc.print(gameData.game(), null);
        }
    }

    private void leave(String[] params) throws IOException {
        if(params.length > 0){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("leave doesn't take any inputs\n");
            return;
        }
        ws.leave(authToken, gameData.gameID());
        check = false;
    }

    private void move(String[] params) throws IOException, DataFormatException, InvalidMoveException {
        if(params.length != 2){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("move only takes two inputs\n");
            return;
        }
        if((!checkIfBlack && gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK) || (checkIfBlack && gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE)){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("not your turn!\n");
            return;
        }

        String start = params[0];
        String end = params[1];
        ChessPosition startPos = parsePosition(start);
        ChessPosition endPos = parsePosition(end);
        if(!checkPositionValid(start)){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("not a valid chess position\n");
            return;
        }
        if(gameData.game().validMoves(startPos) == null){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("no piece in that position\n");
            return;
        }
        String promotion = null;
        ChessPiece.PieceType type = null;
        if(gameData.game().getBoard().getPiece(startPos).getPieceType() == ChessPiece.PieceType.PAWN  && (endPos.getRow() == 1|| endPos.getRow() == 8)){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Promotion Piece for pawn: \n");
            promotion = scanner.nextLine();
            if(promotion.equalsIgnoreCase("queen")){
                type = ChessPiece.PieceType.QUEEN;
            } else if (promotion.equalsIgnoreCase("rook")) {
                type = ChessPiece.PieceType.ROOK;
            } else if (promotion.equalsIgnoreCase("knight")) {
                type = ChessPiece.PieceType.KNIGHT;
            } else if (promotion.equalsIgnoreCase("bishop")) {
                type = ChessPiece.PieceType.BISHOP;
            }
        }
        ChessMove move = new ChessMove(startPos, endPos, type);
        ws.makeMove(authToken, gameData.gameID(), move);
        //gameData.game().makeMove(move);
        updateGame();
    }

    private void resign(String[] params) throws IOException {
        if(params.length > 0){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("resign doesn't take any inputs\n");
            return;
        }
        ws.resign(authToken, gameData.gameID());
    }

    private void highlight(String[] params){
        String start = params[0];
        if(params.length > 1){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("highlight only takes one input\n");
            return;
        }
        if(!checkPositionValid(start)){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("not a valid chess position\n");
            return;
        }
        ChessPosition startPos = parsePosition(start);
        if(gameData.game().validMoves(startPos) == null){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("no piece in that position\n");
            return;
        }
        if(gameData.game().validMoves(startPos).isEmpty()){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("this piece has no valid moves dummy\n");
            return;
        }
        if(checkIfBlack){
            printFunc.printBack(gameData.game(), (ArrayList<ChessMove>) gameData.game().validMoves(startPos));
        } else {
            printFunc.print(gameData.game(), (ArrayList<ChessMove>) gameData.game().validMoves(startPos));
        }
    }

    private void updateGame() throws DataFormatException {
        //ArrayList<GameData> lis = ws.list
        LogoutRequest req = new LogoutRequest(authToken);
        ListResponse res = server.listGames(req);
        if(res == null){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("cannot update game\n");
            return;
        }
        gameData = res.games().get(gameData.gameID()-1);
    }

    public void notifyError(ErrorMessage notification) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        System.out.println(notification.getMessage());
        System.out.println("\n");
    }

    public void notifyMessage(NotificationMessage notification) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
        System.out.println(notification.getMessage());
        System.out.println("\n");
    }

    public void notifyGame(ServerMessage notification) throws DataFormatException {
        //printFunc.print(notification.getMessage(), null);
        updateGame();
        System.out.println("\n");
        //printPrompt();
    }

    @Override
    public void notify(ServerMessage notification, String message) throws DataFormatException {
        //System.out.println("testNotify\n");
        if(notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            notifyMessage((NotificationMessage) notification);
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            notifyError((ErrorMessage) notification);
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            notifyGame(notification);
        }
    }

    private ChessPosition parsePosition(String start) {
        int startRow = 0;
        int startCol = 0;
        if(start.equalsIgnoreCase("a1")) {
            startRow = 1;
            startCol = 1;
        } else if (start.equalsIgnoreCase("a2")) {
            startCol = 2;
            startRow = 1;
        }  else if (start.equalsIgnoreCase("a3")) {
            startCol = 3;
            startRow = 1;
        } else if (start.equalsIgnoreCase("a4")) {
            startCol = 4;
            startRow = 1;
        } else if (start.equalsIgnoreCase("a5")) {
            startCol = 5;
            startRow = 1;
        } else if (start.equalsIgnoreCase("a6")) {
            startCol = 6;
            startRow = 1;
        } else if (start.equalsIgnoreCase("a7")) {
            startCol = 7;
            startRow = 1;
        } else if (start.equalsIgnoreCase("a8")) {
            startCol = 8;
            startRow = 1;
        } else if (start.equalsIgnoreCase("b1")) {
            startRow = 2;
            startCol = 1;
        } else if (start.equalsIgnoreCase("b2")) {
            startRow = 2;
            startCol = 2;
        } else if (start.equalsIgnoreCase("b3")) {
            startRow = 2;
            startCol = 3;
        } else if (start.equalsIgnoreCase("b4")) {
            startRow = 2;
            startCol = 4;
        } else if (start.equalsIgnoreCase("b5")) {
            startRow = 2;
            startCol = 5;
        } else if (start.equalsIgnoreCase("b6")) {
            startRow = 2;
            startCol = 6;
        } else if (start.equalsIgnoreCase("b7")) {
            startRow = 2;
            startCol = 7;
        } else if (start.equalsIgnoreCase("b8")) {
            startRow = 2;
            startCol = 8;
        }  else if (start.equalsIgnoreCase("c1")) {
            startRow = 3;
            startCol = 1;
        } else if (start.equalsIgnoreCase("c2")) {
            startRow = 3;
            startCol = 2;
        } else if (start.equalsIgnoreCase("c3")) {
            startRow = 3;
            startCol = 3;
        } else if (start.equalsIgnoreCase("c4")) {
            startRow = 3;
            startCol = 4;
        } else if (start.equalsIgnoreCase("c5")) {
            startRow = 3;
            startCol = 5;
        } else if (start.equalsIgnoreCase("c6")) {
            startRow = 3;
            startCol = 6;
        } else if (start.equalsIgnoreCase("c7")) {
            startRow = 3;
            startCol = 7;
        } else if (start.equalsIgnoreCase("c8")) {
            startRow = 3;
            startCol = 8;
        }  else if (start.equalsIgnoreCase("d1")) {
            startRow = 4;
            startCol = 1;
        } else if (start.equalsIgnoreCase("d2")) {
            startRow = 4;
            startCol = 2;
        } else if (start.equalsIgnoreCase("d3")) {
            startRow = 4;
            startCol = 3;
        } else if (start.equalsIgnoreCase("d4")) {
            startRow = 4;
            startCol = 4;
        } else if (start.equalsIgnoreCase("d5")) {
            startRow = 4;
            startCol = 5;
        } else if (start.equalsIgnoreCase("d6")) {
            startRow = 4;
            startCol = 6;
        } else if (start.equalsIgnoreCase("d7")) {
            startRow = 4;
            startCol = 7;
        } else if (start.equalsIgnoreCase("d8")) {
            startRow = 4;
            startCol = 8;
        }  else if (start.equalsIgnoreCase("e1")) {
            startRow = 5;
            startCol = 1;
        } else if (start.equalsIgnoreCase("e2")) {
            startRow = 5;
            startCol = 2;
        } else if (start.equalsIgnoreCase("e3")) {
            startRow = 5;
            startCol = 3;
        } else if (start.equalsIgnoreCase("e4")) {
            startRow = 5;
            startCol = 4;
        } else if (start.equalsIgnoreCase("e5")) {
            startRow = 5;
            startCol = 5;
        } else if (start.equalsIgnoreCase("e6")) {
            startRow = 5;
            startCol = 6;
        } else if (start.equalsIgnoreCase("e7")) {
            startRow = 5;
            startCol = 7;
        } else if (start.equalsIgnoreCase("e8")) {
            startRow = 5;
            startCol = 8;
        } else if (start.equalsIgnoreCase("f1")) {
            startRow = 6;
            startCol = 1;
        } else if (start.equalsIgnoreCase("f2")) {
            startRow = 6;
            startCol = 2;
        } else if (start.equalsIgnoreCase("f3")) {
            startRow = 6;
            startCol = 3;
        } else if (start.equalsIgnoreCase("f4")) {
            startRow = 6;
            startCol = 4;
        } else if (start.equalsIgnoreCase("f5")) {
            startRow = 6;
            startCol = 5;
        } else if (start.equalsIgnoreCase("f6")) {
            startRow = 6;
            startCol = 6;
        } else if (start.equalsIgnoreCase("f7")) {
            startRow = 6;
            startCol = 7;
        } else if (start.equalsIgnoreCase("f8")) {
            startRow = 6;
            startCol = 8;
        } else if (start.equalsIgnoreCase("g1")) {
            startRow = 7;
            startCol = 1;
        } else if (start.equalsIgnoreCase("g2")) {
            startRow = 7;
            startCol = 2;
        } else if (start.equalsIgnoreCase("g3")) {
            startRow = 7;
            startCol = 3;
        } else if (start.equalsIgnoreCase("g4")) {
            startRow = 7;
            startCol = 4;
        } else if (start.equalsIgnoreCase("g5")) {
            startRow = 7;
            startCol = 5;
        } else if (start.equalsIgnoreCase("g6")) {
            startRow = 7;
            startCol = 6;
        } else if (start.equalsIgnoreCase("g7")) {
            startRow = 7;
            startCol = 7;
        } else if (start.equalsIgnoreCase("g8")) {
            startRow = 7;
            startCol = 8;
        } else if (start.equalsIgnoreCase("h1")) {
            startRow = 8;
            startCol = 1;
        } else if (start.equalsIgnoreCase("h2")) {
            startRow = 8;
            startCol = 2;
        } else if (start.equalsIgnoreCase("h3")) {
            startRow = 8;
            startCol = 3;
        } else if (start.equalsIgnoreCase("h4")) {
            startRow = 8;
            startCol = 4;
        } else if (start.equalsIgnoreCase("h5")) {
            startRow = 8;
            startCol = 5;
        } else if (start.equalsIgnoreCase("h6")) {
            startRow = 8;
            startCol = 6;
        } else if (start.equalsIgnoreCase("h7")) {
            startRow = 8;
            startCol = 7;
        } else if (start.equalsIgnoreCase("h8")) {
            startRow = 8;
            startCol = 8;
        }
        return new ChessPosition(startCol, startRow);
    }

    private boolean checkPositionValid(String position){
        if(position.length() != 2){
            return false;
        }
        Character[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        Character[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8'};
        List<Character> lets = Arrays.asList(letters);
        List<Character> nums = Arrays.asList(numbers);
        if(!lets.contains(position.charAt(0)) || !nums.contains(position.charAt(1))){
            return false;
        }
        return true;
    }
}
