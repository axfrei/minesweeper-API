package com.deviget.minesweeper.controller;

import java.util.List;

import com.deviget.minesweeper.dto.GameRequest;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

  @Autowired
  GameService gameservice;

  @PostMapping("/create")
  public Game createGame(@RequestBody final GameRequest newGameRequest){
    return gameservice.createGame(newGameRequest);
  }

  @PostMapping
  public Game saveGame(@RequestBody final Game game) {
    return gameservice.saveGame(game);
  }

  @GetMapping("/{userid}")
  public List<Game> getGamesByUserId(@PathVariable("userid") final String userId) {
    return gameservice.getGamesByUserId(userId);
  }

}
