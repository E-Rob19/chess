package dataaccess;

import chess.ChessGame;
import model.GameData;
import requests.GameDataShort;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDataAccess{

    private static final ArrayList<GameData> GAMES = new ArrayList<>();
    static int gameNum = 1;

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : GAMES) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        GAMES.add(new GameData(gameNum, null, null, gameName, new ChessGame()));
        gameNum = gameNum + 1;
        return gameNum-1;
    }


//    public break ArrayList<GameDataShort> listGamesForResponse() throws DataAccessException {
//        ArrayList<GameDataShort> lis = new ArrayList<>();
//        for(GameData game : GAMES){
//          lis.add(new GameDataShort(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
//        }
//        return lis;
//    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException{
        return GAMES;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) {
        for (GameData game : GAMES) {
            if (game.gameID() == gameID) {
                if(playerColor.equals("WHITE")){
                    GAMES.set( GAMES.indexOf(game), new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()) );
                    //(game.whiteUsername() == null || game.whiteUsername().equals(username))
                } else if (playerColor.equals("BLACK")) {
                    GAMES.set( GAMES.indexOf(game), new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()) );
                }
            }
        }
    }

    @Override
    public void clear() throws DataAccessException {
        GAMES.clear();
        gameNum = 1;
    }
}
