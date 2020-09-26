package com.deviget.minesweeper.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;

import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.error.MinesweeperApiException;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.model.GameStatus;
import com.deviget.minesweeper.model.User;
import com.deviget.minesweeper.repository.GameRepository;

import org.apache.logging.log4j.util.Timer.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    private GameRequest newGameRequest;

    private Game gameCreated;

    private final String USER_NAME = "AN_USER";

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().id(USER_NAME).build();
        newGameRequest = GameRequest.builder().bombs(5).columns(5).rows(5).user(user).build();
        gameCreated = gameService.generateGame(newGameRequest);
    }

    @Test
    void valeidGameRequestSavesNewGame(){
        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(this.gameCreated);
        
        Game game = gameService.createGame(newGameRequest);

        assertNotNull(game);
        assertEquals(newGameRequest.getRows() * newGameRequest.getColumns(), game.getCells().size());
        assertEquals(newGameRequest.getBombs(), game.getBombs());
        assertEquals(newGameRequest.getUser(), game.getUser());
    }

    @Test
    void invalidGameRequestThrowsMinesweeperApiException(){
        GameRequest gameRequest = GameRequest.builder().bombs(999).rows(5).columns(5).build();
        
        try{
            gameService.createGame(gameRequest);
            fail();
        } catch (MinesweeperApiException e) {
            assertEquals(e.getMessage(),"Invalid request. Amount of bombs should be less than total amount of cells");
        }
    }

    
    @Test
    void pauseActiveGame(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        Game gamePaused = gameService.pause(gameId);

        assertEquals(gamePaused.getStatus(), GameStatus.PAUSED);
    }

    @Test
    void resumeAPausedGame(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        this.gameCreated.setStatus(GameStatus.PAUSED);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        Game gamePaused = gameService.pause(gameId);

        assertEquals(gamePaused.getStatus(), GameStatus.ACTIVE);
    }

    @Test
    void pauseAnOverGameThrowsMinesweeperApiException(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        this.gameCreated.setStatus(GameStatus.OVER);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        try{
            gameService.pause(gameId);
            fail();
        } catch(MinesweeperApiException e){
            assertEquals(e.getMessage(),"The game is over and could not be resumed/paused");
        }
    }

}
