package com.deviget.minesweeper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CellRequest {
    
    String gameId;

    int x;

    int y;


}
