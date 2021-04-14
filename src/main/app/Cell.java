package app;

public class Cell {

    private boolean isMine = false;
    private short minesAround;
    private boolean isFlag = false;

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public short getMinesAround() {
        return minesAround;
    }

    public void incMinesAround () {
        minesAround++;
    }

}
