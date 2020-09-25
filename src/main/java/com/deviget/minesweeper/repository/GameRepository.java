package com.deviget.minesweeper.repository;

import java.util.List;

import com.deviget.minesweeper.model.Game;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface GameRepository extends CrudRepository<Game, String> {
    
    List<Game> findByUserId(String userId);
}
