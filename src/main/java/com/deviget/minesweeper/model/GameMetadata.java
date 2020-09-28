package com.deviget.minesweeper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@DynamoDBDocument
public class GameMetadata {
    
    private int columns;

    private int rows;

    private int bombs;

    public GameMetadata() {}
}
