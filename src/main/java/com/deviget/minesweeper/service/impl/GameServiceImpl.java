package com.deviget.minesweeper.service.impl;

import java.util.List;
import java.util.Optional;

import com.deviget.minesweeper.dto.CellRequest;
import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.error.MinesweeperApiException;
import com.deviget.minesweeper.model.Cell;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.model.GameStatus;
import com.deviget.minesweeper.repository.GameRepository;
import com.deviget.minesweeper.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Override
    public Game saveGame(final Game newGame) {
        return gameRepository.save(newGame);
    }

    @Override
    public List<Game> getGamesByUserId(final String userId) {
        return gameRepository.findByUserId(userId);
    }

    @Override
    public Game createGame(GameRequest newGameRequest) {
        return saveGame(generateGame(newGameRequest));
    }

    @Override
    public Game generateGame(GameRequest newGameRequest) {
        Game newGame = Game.builder().status(GameStatus.ACTIVE).userId(newGameRequest.getUserId()).build();
        newGame.initCells(newGameRequest.getColumns(), newGameRequest.getRows(), newGameRequest.getBombs());
        return newGame;
    }

    @Override
    public Game getGameById(String gameId) {
        Optional<Game> response = gameRepository.findById(gameId);
        return response.orElseThrow(() -> new MinesweeperApiException("Game with id does not exist"));
    }

    @Override
    public Game pause(String gameId) {
        Game game = getGameById(gameId);
        game.pause();
        return this.gameRepository.save(game);
    }

    @Override
    public Game flagCell(CellRequest cellRequest) {
        Game game = getGameById(cellRequest.getGameId());
        Cell cell = game.getCell(cellRequest.getX(), cellRequest.getY());
        cell.flag();
        return saveGame(game);
    }

    @Override
    public Game recognizeCell(CellRequest cellRequest) {
        Game game = getGameById(cellRequest.getGameId());
        Cell cell = game.getCell(cellRequest.getX(), cellRequest.getY());
        game.recognizeCell(cell);
        return saveGame(game);
    }

}
