package com.deviget.minesweeper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Data;

@Data
@DynamoDBDocument
public class Cell {
    
    private int x;
    
    private int y;

    private long value;

    //do I need this 3 or could I manage this with value attribute (eg: -1 flagged, -2 bomb. -3 recognized)
    private boolean flagged;

    private boolean bomb;

    private boolean recognized;

    
}
