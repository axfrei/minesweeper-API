package com.deviget.minesweeper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;

import com.deviget.minesweeper.dto.CellRequest;
import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.error.MinesweeperApiException;
import com.deviget.minesweeper.model.Cell;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.model.GameStatus;
import com.deviget.minesweeper.repository.GameRepository;

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

    private final String USER_ID = "AN_USER";

    private Cell cellWithBomb;

    private Cell cellWitValue;

    private Cell cellBlank;

    @BeforeEach
    public void setup() {
        newGameRequest = GameRequest.builder().bombs(5).columns(10).rows(10).userId(USER_ID).build();
        gameCreated = gameService.generateGame(newGameRequest);

        cellWithBomb = gameCreated.getCells().stream().filter(c -> c.isBomb()).findFirst().orElseThrow();
        cellWitValue = gameCreated.getCells().stream().filter(c -> !c.isBomb() && c.getValue()>0).findFirst().orElseThrow();
        cellBlank = gameCreated.getCells().stream().filter(c -> !c.isBomb() && c.getValue()==0).findFirst().orElseThrow();
    }

    @Test
    void validGameRequestSavesNewGame(){
        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(this.gameCreated);
        
        Game game = gameService.createGame(newGameRequest);

        assertNotNull(game);
        assertEquals(newGameRequest.getRows() * newGameRequest.getColumns(), game.getCells().size());
        assertEquals(newGameRequest.getBombs(), game.bombsAmount());
        assertEquals(newGameRequest.getUserId(), game.getUserId());
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
        this.gameCreated.setStatus(GameStatus.GAME_OVER);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        try{
            gameService.pause(gameId);
            fail();
        } catch(MinesweeperApiException e){
            assertEquals(e.getMessage(),"The game is over and could not be resumed/paused");
        }
    }

    @Test
    void flagCell(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        CellRequest cellRequest = CellRequest.builder().gameId(gameId).x(0).y(0).build();
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        assertTrue(gameService.flagCell(cellRequest).getCell(0, 0).isFlagged());
    }

    @Test
    void unflagCell(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        gameCreated.getCell(0,0).flag();

        CellRequest cellRequest = CellRequest.builder().gameId(gameId).x(0).y(0).build();
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        assertFalse(gameService.flagCell(cellRequest).getCell(0, 0).isFlagged());
    }

    @Test
    void flagOutOfIndexCellThrowsMinesweeperApiException(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        CellRequest cellRequest = CellRequest.builder().gameId(gameId).x(99).y(99).build();
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        try{
            gameService.flagCell(cellRequest);
            fail();
        } catch(MinesweeperApiException e) {
            assertEquals(e.getMessage(), "Requested cell is out of index");
        }
    }

    @Test
    void recognizeCellThatIsNotBlankJustRecognizeOnlyOneCell(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        int previusRecognizedAmount = gameCreated.getCells().stream().map(c -> c.isRecognized() ? 1 : 0).reduce(0,Integer::sum);

        gameService.recognizeCell(CellRequest.builder().gameId(gameId).x(cellWitValue.getX()).y(cellWitValue.getY()).build());

        int recognizedAmount = gameCreated.getCells().stream().map(c -> c.isRecognized() ? 1 : 0).reduce(0,Integer::sum);
        Cell recognizedCell = gameCreated.getCells().stream().filter(c -> c.isRecognized()).findFirst().orElseThrow();

        assertEquals(recognizedAmount, previusRecognizedAmount+1);
        assertEquals(recognizedCell.getX(), cellWitValue.getX());
        assertEquals(recognizedCell.getY(), cellWitValue.getY());
    }

    @Test
    void recognizeCellThatIsBlank(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);

        Game game = gameService.recognizeCell(CellRequest.builder().gameId(gameId).x(cellBlank.getX()).y(cellBlank.getY()).build());

        game.getAdjacentCellsStream(cellBlank).filter(c -> c.getValue()==0 && !c.isBomb()).forEach(c-> assertTrue(c.isRecognized()));
    }

    @Test
    void recognizeCellThatIsBombChangeGameStatusToGameOVer(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        Game game = gameService.recognizeCell(CellRequest.builder().gameId(gameId).x(cellWithBomb.getX()).y(cellWithBomb.getY()).build());

        assertEquals(game.getStatus(), GameStatus.GAME_OVER);
    }

    @Test
    void recognizeCellThatIsOutOfIndexThrowsMinesweeperApiException(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        CellRequest cellRequest = CellRequest.builder().gameId(gameId).x(99).y(99).build();
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);
        
        try{
            gameService.recognizeCell(cellRequest);
            fail();
        } catch(MinesweeperApiException e) {
            assertEquals(e.getMessage(), "Requested cell is out of index");
        }
    }

    @Test
    void recognizeAllNotBombCellsMakesYouAWinner(){
        String gameId = UUID.randomUUID().toString();
        this.gameCreated.setId(gameId);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(gameCreated));
        Mockito.when(gameRepository.save(eq(gameCreated))).thenReturn(gameCreated);

        Game finalScore = gameCreated.getCells().stream().filter(c -> !c.isBomb()).map(notBomb -> {
            Game game = gameService.recognizeCell(CellRequest.builder().gameId(gameId).x(notBomb.getX()).y(notBomb.getY()).build());
            return game;
        }).reduce((a, b) -> a).orElseThrow();
        
        assertEquals(finalScore.getStatus(), GameStatus.WIN);
    }

}
