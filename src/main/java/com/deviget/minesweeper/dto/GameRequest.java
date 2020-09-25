package com.deviget.minesweeper.dto;

import com.deviget.minesweeper.model.User;

import lombok.Data;

@Data
public class GameRequest {
    
    private int columns;

    private int rows;

    private int bombs;

    private User user;

}
