package app;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameField {

    public final int fieldWidth;
    public final int fieldHeight;
    public final int minesNumber;
    public final Cell[][] matrix;

    public GameField(int fieldWidth, int fieldHeight, int minesNumber) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.minesNumber = minesNumber;
        this.matrix = createField(new Cell[][]{});
    }

    public Cell[][] createField(Cell[][] matrix) {
        Set<Integer> helper = new HashSet<>();
        Random rnd = new Random(System.currentTimeMillis());
        while (helper.size() != minesNumber)
            helper.add(rnd.nextInt(fieldWidth * fieldHeight));
        int counter = 0;

        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {
                if (helper.contains(counter)) matrix[i][j].setMine(true);

                if (i == 0) {
                    if (j == 0) {
                        matrix[0][1].incMinesAround();
                        matrix[1][0].incMinesAround();
                    } else if (j == fieldWidth - 1) {
                        matrix[0][j].incMinesAround();
                        matrix[1][j - 1].incMinesAround();
                        matrix[1][j].incMinesAround();
                    } else {
                        matrix[0][j - 1].incMinesAround();
                        matrix[0][j + 1].incMinesAround();
                        matrix[1][j - 1].incMinesAround();
                        matrix[1][j].incMinesAround();
                    }
                } else if ((fieldHeight & 2) == 0) {
                    if (i == fieldHeight - 1) {
                        if (j == 0) {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                        } else if (j == fieldWidth - 1) {
                            matrix[i - 1][j - 1].incMinesAround();
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                        } else {
                            matrix[i - 1][j - 1].incMinesAround();
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                        }
                    } else {
                        if (j == 0) {
                            matrix[i - 1][0].incMinesAround();
                            matrix[i][1].incMinesAround();
                            matrix[i + 1][0].incMinesAround();
                        } else if (j == fieldWidth - 1) {
                            matrix[i - 1][j - 1].incMinesAround();
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i + 1][j - 1].incMinesAround();
                            matrix[i + 1][j].incMinesAround();
                        } else {
                            matrix[i - 1][j - 1].incMinesAround();
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                            matrix[i + 1][j - 1].incMinesAround();
                            matrix[i + 1][j].incMinesAround();
                        }
                    }
                } else {
                    if (i == fieldHeight - 1) {
                        if (j == 0) {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i - 1][j + 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                        } else if (j == fieldWidth - 1) {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                        } else {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i - 1][j + 1].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                        }
                    } else {
                        if (j == 0) {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i - 1][j + 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                            matrix[i + 1][j].incMinesAround();
                            matrix[i + 1][j + 1].incMinesAround();
                        } else if (j == fieldWidth - 1) {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i + 1][j].incMinesAround();
                        } else {
                            matrix[i - 1][j].incMinesAround();
                            matrix[i - 1][j + 1].incMinesAround();
                            matrix[i][j - 1].incMinesAround();
                            matrix[i][j + 1].incMinesAround();
                            matrix[i + 1][j].incMinesAround();
                            matrix[i + 1][j + 1].incMinesAround();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {

            }
        }
        return matrix;
    }

}
