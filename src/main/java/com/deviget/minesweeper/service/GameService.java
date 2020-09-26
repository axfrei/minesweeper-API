package com.deviget.minesweeper.service;

import java.util.List;

import com.deviget.minesweeper.dto.CellRequest;
import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.model.Game;

public interface GameService {

	Game saveGame(Game newGame);

	List<Game> getGamesByUserId(String userId);

	Game createGame(GameRequest newGameRequest) ;

	Game generateGame(GameRequest newGameRequest);

	Game getGameById(String gameId);

	Game pause(String gameId);

	Game flagCell(CellRequest cellRequest);

	Game recognizeCell(CellRequest cellRequest);
    
}
