package com.deviget.minesweeper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    
    private String message;
    
}
