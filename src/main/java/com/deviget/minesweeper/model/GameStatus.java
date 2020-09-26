package com.deviget.minesweeper.model;

import com.deviget.minesweeper.error.MinesweeperApiException;

public enum GameStatus {
    ACTIVE {
        @Override
        GameStatus pause() {
            return GameStatus.PAUSED;
        };
    },
    PAUSED {
        @Override
        GameStatus pause() {
            return GameStatus.ACTIVE;
        };
    },
    GAME_OVER {
        @Override
        GameStatus pause() {
            throw new MinesweeperApiException("The game is over and could not be resumed/paused");
        };
    },
    WIN {
        @Override
        GameStatus pause() {
            throw new MinesweeperApiException("The game is over and could not be resumed/paused");
        };
    };

    abstract GameStatus pause();
}
