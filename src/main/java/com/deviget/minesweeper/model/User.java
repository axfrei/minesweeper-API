package com.deviget.minesweeper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Data;

@Data
@DynamoDBDocument
public class User {

    private String id;
    
}
