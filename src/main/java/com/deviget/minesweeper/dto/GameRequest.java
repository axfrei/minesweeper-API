package com.deviget.minesweeper.dto;

import com.deviget.minesweeper.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameRequest {
    
    private int columns;

    private int rows;

    private int bombs;

    private User user;

}
