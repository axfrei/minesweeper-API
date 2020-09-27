package com.deviget.minesweeper.controller;

import java.util.List;

import com.deviget.minesweeper.dto.CellRequest;
import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/game")
public class GameController {

  @Autowired
  GameService gameService;

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Creates a new minesweeper game", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Game has been created successfully") })
  public Game createGame(@RequestBody final GameRequest newGameRequest){
    return gameService.createGame(newGameRequest);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Saves the game", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Games has been saved successfully") })
  public Game saveGame(@RequestBody final Game game) {
    return gameService.saveGame(game);
  }

  @GetMapping("/{userid}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets all the games by user Id", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Games has been fetched successfully") })
  public List<Game> getGamesByUserId(@PathVariable("userid") final String userId) {
    return gameService.getGamesByUserId(userId);
  }

  @GetMapping("/load/{gameId}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Load a previous game by Id", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Game loaded successfully") })
  public Game loadGame(@PathVariable("gameId") final String gameId){
    return gameService.getGameById(gameId);
  }

  @PutMapping("/pause/{gameId}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Pause/resume a game by Id", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Game has been paused/resumed successfully") })
  public Game pauseGame(@PathVariable("gameId") final String gameId){
    return gameService.pause(gameId);
  }

  @PutMapping("/flag")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Flag a cell", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Cell has been flagged successfully") })
  public Game flagCell(@RequestBody final CellRequest cellRequest){
    return gameService.flagCell(cellRequest);
  }

  @PutMapping("/recognize")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Recognize a cell", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Cell has been recognized successfully") })
  public Game recognizeCell(@RequestBody final CellRequest cellRequest){
    return gameService.recognizeCell(cellRequest);
  }
}
