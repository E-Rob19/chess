package dataaccess;

import chess.ChessGame;
import model.GameData;
import requests.GameDataShort;

import java.util.ArrayList;

public interface GameDataAccess {
    GameData getGame(int gameID) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    //ArrayList<GameDataShort> listGamesForResponse() throws DataAccessException

    void updateGame(int gameID, ChessGame game, ChessGame.TeamColor color) throws DataAccessException ;

    void addPlayer(int gameID, String username, String playerColor) throws DataAccessException;

    void clear() throws DataAccessException;

    //createGame: Create a new game.
    //getGame: Retrieve a specified game with the given game ID.
    //listGames: Retrieve all games.
    //updateGame: Updates a chess game. It should replace the chess game string
    //corresponding to a given gameID. This is used when players join a game or when a move is made.

}
