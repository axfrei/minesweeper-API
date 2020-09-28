package com.deviget.minesweeper.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.deviget.minesweeper.error.MinesweeperApiException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@DynamoDBTable(tableName = "MinesweeperApi.Games")
@Data
@AllArgsConstructor
@Builder
public class Game {

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

    @DynamoDBAttribute(attributeName = "metadata")
    private GameMetadata metadata;

    @DynamoDBAttribute(attributeName = "cells")
    private List<Cell> cells;

    @DynamoDBTyped(DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName = "status")
    private GameStatus status;

    @DynamoDBTyped(DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName = "timeConsumed")
    @Builder.Default
    private long timePaused = 0L;

    @DynamoDBAttribute(attributeName = "creationTime")
    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    private Date creationTime;

    @DynamoDBAttribute(attributeName = "lastUpdate")
    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
    private Date lastUpdate;

    public Game() {
    }

    public Game initCells(int columns, int rows, int bombs) {

        this.setMetadata(GameMetadata.builder().rows(rows).columns(columns).bombs(bombs).build());

        if (columns * rows <= bombs) {
            throw new MinesweeperApiException(
                    "Invalid request. Amount of bombs should be less than total amount of cells");
        }

        // lets distribute the bombs randomly
        Set<Integer> bombDistribution = new HashSet<Integer>();
        while (bombDistribution.size() < bombs) {
            bombDistribution.add((int) (Math.random() * ((rows * columns) - 1)));
        }

        this.cells = new ArrayList<Cell>();
        // lets create cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                cells.add(Cell.builder().x(r).y(c).bomb(bombDistribution.contains((r * rows) + c)).build());
            }
        }

        // lets calculate the value for each cell
        cells.stream().forEach(cellToInit -> {
            int value = getAdjacentCellsStream(cellToInit).map(c -> c.isBomb() ? 1 : 0).reduce(0, Integer::sum);
            cellToInit.setValue(value);
        });

        return this;
    }

    public Stream<Cell> getAdjacentCellsStream(Cell cell) {
        return cells.stream().filter(c -> c.isAdjacentTo(cell));
    }

    public void pause() {
        if (!this.getStatus().equals(GameStatus.PAUSED)) {
            acumulateTipoPaused();
        }
        this.setStatus(this.status.pause());
    }

    private void acumulateTipoPaused() {
        Calendar initDate = Calendar.getInstance();
        initDate.setTime(this.lastUpdate != null ? this.lastUpdate : this.creationTime);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        long diffSeconds = (endDate.getTimeInMillis() - initDate.getTimeInMillis()) / 1000;
        this.timePaused = this.timePaused + diffSeconds;
    }

    public Long getTimePlayed() {
        Calendar initDate = Calendar.getInstance();
        initDate.setTime(this.creationTime);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(this.getStatus().isEnded()?this.getLastUpdate():new Date());
        long diffSeconds = (endDate.getTimeInMillis() - initDate.getTimeInMillis()) / 1000;
        return diffSeconds - getTimePaused();
    }

    public Game recognizeCell(Cell cell) {
        cell.setRecognized(true);

        if (cell.isBomb()) {
            // game over
            this.setStatus(GameStatus.GAME_OVER);
            return this;
        }

        if (cell.getValue() == 0) {
            this.recognizeAdjacentCells(cell);
        }

        int totalRecognized = cells.stream().map(c -> c.isRecognized() ? 1 : 0).reduce(0, Integer::sum);
        int totalBombs = bombsAmount();

        if (totalRecognized + totalBombs == cells.size()) {
            this.setStatus(GameStatus.WIN);
        }

        return this;
    }

    public void recognizeAdjacentCells(Cell aCell) {
        getAdjacentCellsStream(aCell).filter(adjCell -> !adjCell.isRecognized() && !adjCell.isBomb())
                .forEach(adjEmptyCell -> {
                    adjEmptyCell.setRecognized(true);
                    if (adjEmptyCell.getValue() == 0) {
                        recognizeAdjacentCells(adjEmptyCell);
                    }
                });
    }

    public Integer bombsAmount() {
        return cells.stream().map(c -> c.isBomb() ? 1 : 0).reduce(0, Integer::sum);
    }

    public Cell getCell(int x, int y) {
        return cells.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst()
                .orElseThrow(() -> new MinesweeperApiException("Requested cell is out of index"));
    }
}