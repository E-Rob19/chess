package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import service.GameDataShort;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDataAccess{

    private static final ArrayList<GameData> games = new ArrayList<>();
    int gameNum = 1;

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        games.add(new GameData(gameNum, null, null, gameName, new ChessGame()));
        gameNum++;
        return gameNum-1;
    }

    @Override
    public ArrayList<GameDataShort> listGamesForResponse() throws DataAccessException {
        ArrayList<GameDataShort> lis = new ArrayList<>();
        for(GameData game : games){
          lis.add(new GameDataShort(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return lis;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException{
        return games;
    }

    @Override
    public void addPlayer(int gameID, String username, String playerColor) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                if(playerColor.equals("WHITE")){
                    games.set( games.indexOf(game), new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()) );
                    //(game.whiteUsername() == null || game.whiteUsername().equals(username))
                } else if (playerColor.equals("BLACK")) {
                    games.set( games.indexOf(game), new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()) );
                }
            }
        }
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
