package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import facade.ServerFacade;
import model.GameData;
import requests.ListResponse;
import requests.LogoutRequest;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
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
    private GameData gameData;
    boolean checkIfBlack;
    private boolean check = true;
    private PrintChessBoard printFunc = new PrintChessBoard();

    public static void parseInput(String input){
        var token = input.toLowerCase().split(" ");
        params = Arrays.copyOfRange(token, 1, token.length);
        command = (token.length > 0) ? token[0] : "help";
    }

    public void eval(String authToken, ServerFacade server, String username, WebSocketFacade ws, GameData gameData, boolean checkIfBlack) throws DataFormatException, IOException {
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
        String[] lis = {};
        redraw(lis);
        help();
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
        if(checkIfBlack){
            printFunc.printBack(gameData.game());
        } else {
            printFunc.print(gameData.game());
        }
    }

    private void leave(String[] params) throws IOException {
        ws.leave(authToken, gameData.gameID());
        check = false;
    }

    private void move(String[] params) throws IOException, DataFormatException {
        String start = params[0];
        String end = params[1];
        ChessPosition startPos = parsePosition(start);
        ChessPosition endPos = parsePosition(end);
        String promotion = null;
        ChessPiece.PieceType type = null;
        if(gameData.game().getBoard().getPiece(startPos).getPieceType() == ChessPiece.PieceType.PAWN){
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
        updateGame();
    }

    private void resign(String[] params) throws IOException {
        ws.resign(authToken, gameData.gameID());
    }

    private void highlight(String[] params){
        System.out.print("not implemented yet\n");
    }

    private void updateGame() throws DataFormatException {
        //ArrayList<GameData> lis = ws.list
        LogoutRequest req = new LogoutRequest(authToken);
        ListResponse res = server.listGames(req);
        if(res == null){
            System.out.print("cannot update game\n");
            return;
        }
        gameData = res.games().get(gameData.gameID()-1);
    }

    public void notify(ErrorMessage notification) {
        System.out.println(notification.getMessage());
        System.out.println("\n");
    }

    public void notify(LoadGameMessage notification) throws DataFormatException {
        printFunc.print(notification.getMessage());
        updateGame();
        System.out.println("\n");
        //printPrompt();
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println("testNotify\n");
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
}
