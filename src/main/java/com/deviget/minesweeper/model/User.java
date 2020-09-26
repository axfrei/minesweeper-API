package com.deviget.minesweeper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@DynamoDBDocument
public class User {

    private String id;
    
}
