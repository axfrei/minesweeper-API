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

	public boolean isAdjacentTo(Cell cell) {
		return  (this.getX()-cell.getX() == -1 &&  this.getY()-cell.getY() == -1) ||
        (this.getX()-cell.getX() == -1 &&  this.getY()-cell.getY() == 0) ||
        (this.getX()-cell.getX() == -1 &&  this.getY()-cell.getY() == 1) ||
        (this.getX()-cell.getX() == 0 &&  this.getY()-cell.getY() == -1) ||
        (this.getX()-cell.getX() == 0 &&  this.getY()-cell.getY() == 1) ||
        (this.getX()-cell.getX() == 1 &&  this.getY()-cell.getY() == -1) ||
        (this.getX()-cell.getX() == 1 &&  this.getY()-cell.getY() == 0) ||
        (this.getX()-cell.getX() == 1 &&  this.getY()-cell.getY() == 1);
	}

    
}
