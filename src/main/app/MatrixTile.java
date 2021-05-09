package app;

import java.util.ArrayList;
import java.util.List;

public class MatrixTile {

    private final int x;
    private final int y;

    private boolean isMine;
    private Integer minesAround = 0;
    private boolean isOpen;
    private boolean hasFlag;
    private boolean firstTile;

    public MatrixTile(int x, int y){
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFirstTile() {
        return firstTile;
    }

    public void setFirstTile(boolean firstTile) {
        this.firstTile = firstTile;
    }

    public boolean isMine() {
        return isMine;
    }

    public Integer getMinesAround() {
        return minesAround;
    }

    public void incMinesAround() {
        minesAround++;
    }

    public void setFlag(boolean bool) {
        this.hasFlag = bool;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setOpen() {
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<MatrixTile> getNeighbors(MatrixTile[][] matrix) {
        List<MatrixTile> list = new ArrayList<>();
        if (y == 0) { //если первая строка в матрице
            if (x == 0) { //начало строки
                list.add(matrix[1][0]);
                list.add(matrix[0][1]);
            } else if (x == matrix[0].length - 1) { //конец строки
                list.add(matrix[0][x - 1]);
                list.add(matrix[1][x - 1]);
                list.add(matrix[1][x]);
            } else { //остальная часть строки
                list.add(matrix[0][x - 1]);
                list.add(matrix[0][x + 1]);
                list.add(matrix[1][x - 1]);
                list.add(matrix[1][x]);
            }
        } else if (y % 2 == 0) { //если строка четная
            if (y == matrix.length - 1) { //если последняя строка
                if (x == 0) {       //если первая клетка в строке
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x + 1]);
                } else if (x == matrix[0].length - 1) { //последняя клетка в строке
                    list.add(matrix[y - 1][x - 1]);
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                } else {            //остальные клетки
                    list.add(matrix[y - 1][x - 1]);
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y][x + 1]);
                }
            } else {   //если не последняя строка
                if (x == 0) {
                    list.add(matrix[y - 1][0]);
                    list.add(matrix[y][1]);
                    list.add(matrix[y + 1][0]);
                } else if (x == matrix[0].length - 1) {
                    list.add(matrix[y - 1][x - 1]);
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y + 1][x - 1]);
                    list.add(matrix[y + 1][x]);
                } else {
                    list.add(matrix[y - 1][x - 1]);
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y][x + 1]);
                    list.add(matrix[y + 1][x - 1]);
                    list.add(matrix[y + 1][x]);
                }
            }
        } else {
            if (y == matrix.length - 1) {
                if (x == 0) {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y - 1][x + 1]);
                    list.add(matrix[y][x + 1]);
                } else if (x == matrix[0].length - 1) {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                } else {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y - 1][x + 1]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y][x + 1]);
                }
            } else {
                if (x == 0) {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y - 1][x + 1]);
                    list.add(matrix[y][x + 1]);
                    list.add(matrix[y + 1][x]);
                    list.add(matrix[y + 1][x + 1]);
                } else if (x == matrix[0].length - 1) {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y + 1][x]);
                } else {
                    list.add(matrix[y - 1][x]);
                    list.add(matrix[y - 1][x + 1]);
                    list.add(matrix[y][x - 1]);
                    list.add(matrix[y][x + 1]);
                    list.add(matrix[y + 1][x]);
                    list.add(matrix[y + 1][x + 1]);
                }
            }
        }
        return list;
    } //определение соседей клетки

    public void setMine() {
        this.isMine = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof MatrixTile) {
            MatrixTile other = (MatrixTile) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

}