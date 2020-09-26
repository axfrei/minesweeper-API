package com.deviget.minesweeper.controller;

import java.util.List;

import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@
@RestController
@RequestMapping("/game")
public class GameController {

  @Autowired
  GameService gameservice;

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Creates a new minesweeper game", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Game created successfully") })
  public Game createGame(@RequestBody final GameRequest newGameRequest){
    return gameservice.createGame(newGameRequest);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Saves the game", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Games saved successfully") })
  public Game saveGame(@RequestBody final Game game) {
    return gameservice.saveGame(game);
  }

  @GetMapping("/{userid}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets all the games by user Id", response = Game.class, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Games fetched successfully") })
  public List<Game> getGamesByUserId(@PathVariable("userid") final String userId) {
    return gameservice.getGamesByUserId(userId);
  }

}
