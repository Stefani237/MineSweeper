package com.example.dell.minesweeper.Logic;

public class Game {
    private final int MINE = -1;
    private final int EMPTY = 0;

    private Board mBoard;
    private int mDifficulty;
    private boolean mGameOver = false;
    private boolean mWon = false;
    private int mNumOfPressedTiles = 0;

    public Game(int difficulty) {
        mDifficulty = difficulty;
        mBoard = new Board(mDifficulty);
    }


    public boolean ismGameOver() {
        return mGameOver;
    }

    public boolean ismWon() {
        return mWon;
    }

    public void play(int position) {

        int x = position/(mBoard.getmRows());
        int y = position%(mBoard.getmCols());
        revealTile(x,y);

    }

    public void revealTile(int x, int y) {

        if (mBoard.getTile(x, y).isPressed())
            return;

        if(mBoard.getTile(x,y).getTileState() == MINE) { // mine - lose
            mGameOver = true;
            revealAllMines();
            return;
        }

        if(mBoard.getTile(x,y).getTileState() != EMPTY) { // number != 0
            mBoard.getTile(x, y).pressTile();
            mNumOfPressedTiles++;
            if(mNumOfPressedTiles == mBoard.getBoardSize() - mBoard.getNumOfMines()) {// win
                mGameOver = true;
                mWon = true;
            }
            return;
        }

        if(mBoard.getTile(x,y).getTileState() == EMPTY) {
            mBoard.getTile(x, y).pressTile();
            mNumOfPressedTiles++;
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {

                    if((j >= 0 && j < mBoard.getmRows()) && (i >= 0 && i < mBoard.getmCols())) {
                        revealTile(i,j);

                    }
                }
            }
        }
    }

    public void revealAllMines() {
        for (int x = 0; x < mBoard.getmRows(); x++) {
            for (int y = 0; y < mBoard.getmCols(); y++) {
                if ((mBoard.getTile(x, y).getTileState() == MINE))
                    mBoard.getTile(x, y).pressTile();
            }
        }
    }

    public Board getmBoard() {
        return mBoard;
    }

    public boolean boardFullOfMines() {
        int mineCount = 0;

        for (int x = 0; x < mBoard.getmRows(); x++) {
            for (int y = 0; y < mBoard.getmCols(); y++) {
                if ((mBoard.getTile(x, y).hasMine() )) {
                    mineCount++;
                }
            }
        }

        if (mineCount == mBoard.getmCols()*mBoard.getmRows()) {
           // mGameOver = true;
            //revealAllMines();
            return true;
        }
        return false;
    }

}
