package com.deviget.minesweeper.model;

import com.deviget.minesweeper.error.MinesweeperApiException;

public enum GameStatus {
    ACTIVE {
        @Override
        GameStatus pause() {
            return GameStatus.PAUSED;
        }

        @Override
        boolean isEnded() {
           return false;
        };
    },
    PAUSED {
        @Override
        GameStatus pause() {
            return GameStatus.ACTIVE;
        };
        @Override
        boolean isEnded() {
           return false;
        };
    },
    GAME_OVER {
        @Override
        GameStatus pause() {
            throw new MinesweeperApiException("The game is over and could not be resumed/paused");
        };
        @Override
        boolean isEnded() {
           return true;
        };
    },
    WIN {
        @Override
        GameStatus pause() {
            throw new MinesweeperApiException("The game is over and could not be resumed/paused");
        };
        @Override
        boolean isEnded() {
           return true;
        };
    };

    abstract GameStatus pause();
    abstract boolean isEnded();
}
