package com.deviget.minesweeper.service.impl;

import java.util.List;

import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.model.Game;
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
        Game newGame = Game.builder()
            .status("active")
            .user(newGameRequest.getUser())
            .build();
        
        newGame.initCells(newGameRequest.getColumns(), newGameRequest.getRows(), newGameRequest.getBombs());
            
        return saveGame(newGame);
    }
    
}
