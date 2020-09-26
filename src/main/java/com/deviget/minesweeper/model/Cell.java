package com.deviget.minesweeper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@DynamoDBDocument
public class Cell {
    
    private int x;
    
    private int y;

    private long value;

    private boolean flagged;

    private boolean bomb;

    private boolean recognized;

    
}
