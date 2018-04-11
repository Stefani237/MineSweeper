package com.example.dell.minesweeper.Logic;

public class Tile {
    private final int MINE = -1;
    private final int EMPTY = 0;

    private int mTileState;
    private boolean isPressed = false;
    private boolean isFlagged = false;

    public Tile() {
        mTileState = EMPTY;
    }

    public int getTileState() {
        return mTileState;
    }

    public void setTileState(int value) {
        mTileState = value;
    }

    public boolean isEmpty() {
        if (mTileState == EMPTY)
            return true;
        return false;
    }

    public boolean getIsFlagged() {
        if (isFlagged == true) {
            return true;
        }
        return false;
    }

    public void setIsFlagged(boolean flagged) {
        if(flagged == false)
            isFlagged = false;
        else
            isFlagged = true;

    }

    public boolean isPressed() {
        if (isPressed == true)
            return true;

        return false;
    }


    public void pressTile() {
        isPressed = true;

    }

    public void closeTile() {
        isPressed = false;

    }

    public boolean hasMine() {
        if (mTileState == MINE)
            return true;
        return false;
    }

    public String toString() {
        if (isEmpty())
            return " ";

        return String.valueOf(mTileState);

    }
}
