package com.example.dell.minesweeper.Logic;



import java.util.Timer;
import java.util.TimerTask;

public class Board {
    private final int RELAX = 0;
    private final int CHALLENGE = 1;
    private final int MASTER = 2;

    private final int RELAX_SIZE = 8;
    private final int CHALLENGE_SIZE = 10;
    private final int MASTER_SIZE = 12;

    private final int MINES_NUM_RELAX = 8;
    private final int MINES_NUM_CHALLENGE = 10;
    private final int MINES_NUM_MASTER = 12;

    private final int MINE = -1;
    private final int EMPTY = 0;

    private int mBoardSize;
    private int mRows, mCols;
    private Tile[][] mTiles;
    private int mNumOfMines;

    public Board(int difficulty) {
        sizeOfBoard(difficulty);
        mTiles = new Tile[mRows][mCols];
        for (int x = 0; x < mRows; x++) {
            for (int y = 0; y < mCols; y++) {
                mTiles[x][y] = new Tile();
            }
        }
        setTileMine();
        setTileNumber();
    }


    public void sizeOfBoard(int difficulty) {
        if (difficulty == RELAX) {
            mRows = RELAX_SIZE;
            mCols = RELAX_SIZE;
            mBoardSize = RELAX_SIZE*RELAX_SIZE;
            mNumOfMines = MINES_NUM_RELAX;
        } else if (difficulty == CHALLENGE) {
            mRows = CHALLENGE_SIZE;
            mCols = CHALLENGE_SIZE;
            mBoardSize = CHALLENGE_SIZE*CHALLENGE_SIZE;
            mNumOfMines = MINES_NUM_CHALLENGE;
        } else {
            mRows = MASTER_SIZE;
            mCols = MASTER_SIZE;
            mBoardSize = MASTER_SIZE*MASTER_SIZE;
            mNumOfMines = MINES_NUM_MASTER;
        }

    }


    public int getBoardSize() {
        return mBoardSize;
    }


    public Tile getTileByPosition(int position) {
        return mTiles[position / mRows][position % mCols];
    }

    public Tile getTile(int x, int y) {
        return mTiles[x][y];
    }

    public int getmRows() {
        return mRows;
    }


    public int getmCols() {
        return mCols;
    }


    public int getNumOfMines() {
        return mNumOfMines;
    }

    public void setTileMine() {  // puts mine in board
        int locationX = 0;
        int locationY = 0;
        for (int i = 0; i < mNumOfMines; i++) {
            locationX = (int) (Math.random() * mRows);
            locationY = (int) (Math.random() * mCols);
            if (mTiles[locationX][locationY].isEmpty()) {
                mTiles[locationX][locationY].setTileState(-1);

            }

            else
                i--;
        }
    }

    public void setTileNumber() {  // put numbers in board
        int counter = 0;
        for (int x = 0; x < mRows; x++) {
            for (int y = 0; y < mCols; y++) {
                if (mTiles[x][y].getTileState() != MINE) {
                    if (y >= 1 && mTiles[x][y - 1].getTileState() == MINE)
                        counter++;
                    if (y <= mCols - 2 && mTiles[x][y + 1].getTileState() == MINE)
                        counter++;
                    if (x >= 1 && mTiles[x - 1][y].getTileState() == -1)
                        counter++;
                    if (x <= mRows - 2 && mTiles[x + 1][y].getTileState() == MINE)
                        counter++;
                    if (x >= 1 && y >= 1 && mTiles[x - 1][y - 1].getTileState() == MINE)
                        counter++;
                    if (x <= mRows - 2 && y <= mCols - 2 && mTiles[x + 1][y + 1].getTileState() == MINE)
                        counter++;
                    if (x >= 1 && y <= mCols - 2 && mTiles[x - 1][y + 1].getTileState() == MINE)
                        counter++;
                    if (x <= mRows - 2 && y >= 1 && mTiles[x + 1][y - 1].getTileState() == MINE)
                        counter++;
                }
                if (counter != 0) {
                    mTiles[x][y].setTileState(counter);
                    counter = 0;
                }
            }
        }
    }

    public void toggleFlagMark(int x, int y) {
        boolean flagged = mTiles[x][y].getIsFlagged();
        if(flagged == true)
            mTiles[x][y].setIsFlagged(false);
        else
            mTiles[x][y].setIsFlagged(true);
    }

    public void addMoreMine() {
        int locationX = 0;
        int locationY = 0;
        boolean mineAdded = false;

        while(mineAdded == false) {
            locationX = (int) (Math.random() * mRows);
            locationY = (int) (Math.random() * mCols);
            if (!mTiles[locationX][locationY].isPressed() && !mTiles[locationX][locationY].hasMine()) {
                mTiles[locationX][locationY].setTileState(-1);
                setTileNumber();
                mineAdded = true;
            }

        }
    }

    public void closeTile() {
        int locationX = 0;
        int locationY = 0;
        boolean tileClosed = false;


        while(tileClosed == false) {
            locationX = (int) (Math.random() * mRows);
            locationY = (int) (Math.random() * mCols);

            if (mTiles[locationX][locationY].isPressed()) {
                mTiles[locationX][locationY].closeTile();
                tileClosed = true;
            }

        }

    }

    public boolean allTilesClosed() {
        int counter = 0;

        for (int x = 0; x < getmRows(); x++) {
            for (int y = 0; y < getmCols(); y++) {
                if ((!getTile(x, y).isPressed()))
                    counter++;
            }
        }
        if (counter == getmCols()*getmRows())
            return true;
        return false;
    }


}
