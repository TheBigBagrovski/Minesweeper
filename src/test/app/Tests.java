package app;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static app.Game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    private final String[] args = new String[]{""};
    private final List<MatrixTile> expected = new ArrayList<>();
    private List<MatrixTile> actual = new ArrayList<>();

    private void clearExpectedActualLists() {
        actual.clear();
        expected.clear();
    }

    @Test
    public void test_getNeighbors() {
        Main.main(args);
        fieldWidth_input.setText("10");
        fieldHeight_input.setText("10");
        minesNumber_input.setText("10");
        MatrixTile[][] testMatrix = new MatrixTile[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testMatrix[i][j] = new MatrixTile(j, i);
            }
        }
        clearExpectedActualLists();
        actual = testMatrix[0][0].getNeighbors(testMatrix);
        expected.add(testMatrix[1][0]);
        expected.add(testMatrix[0][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[0][5].getNeighbors(testMatrix);
        expected.add(testMatrix[0][4]);
        expected.add(testMatrix[0][6]);
        expected.add(testMatrix[1][4]);
        expected.add(testMatrix[1][5]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[0][9].getNeighbors(testMatrix);
        expected.add(testMatrix[0][8]);
        expected.add(testMatrix[1][8]);
        expected.add(testMatrix[1][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[2][0].getNeighbors(testMatrix);
        expected.add(testMatrix[1][0]);
        expected.add(testMatrix[2][1]);
        expected.add(testMatrix[3][0]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[2][5].getNeighbors(testMatrix);
        expected.add(testMatrix[1][4]);
        expected.add(testMatrix[1][5]);
        expected.add(testMatrix[2][4]);
        expected.add(testMatrix[2][6]);
        expected.add(testMatrix[3][4]);
        expected.add(testMatrix[3][5]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[2][9].getNeighbors(testMatrix);
        expected.add(testMatrix[1][8]);
        expected.add(testMatrix[1][9]);
        expected.add(testMatrix[2][8]);
        expected.add(testMatrix[3][8]);
        expected.add(testMatrix[3][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[5][0].getNeighbors(testMatrix);
        expected.add(testMatrix[4][0]);
        expected.add(testMatrix[4][1]);
        expected.add(testMatrix[5][1]);
        expected.add(testMatrix[6][0]);
        expected.add(testMatrix[6][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[5][5].getNeighbors(testMatrix);
        expected.add(testMatrix[4][5]);
        expected.add(testMatrix[4][6]);
        expected.add(testMatrix[5][4]);
        expected.add(testMatrix[5][6]);
        expected.add(testMatrix[6][5]);
        expected.add(testMatrix[6][6]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[5][9].getNeighbors(testMatrix);
        expected.add(testMatrix[4][9]);
        expected.add(testMatrix[5][8]);
        expected.add(testMatrix[6][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[9][0].getNeighbors(testMatrix);
        expected.add(testMatrix[8][0]);
        expected.add(testMatrix[8][1]);
        expected.add(testMatrix[9][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[9][5].getNeighbors(testMatrix);
        expected.add(testMatrix[8][5]);
        expected.add(testMatrix[8][6]);
        expected.add(testMatrix[9][4]);
        expected.add(testMatrix[9][6]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = testMatrix[9][9].getNeighbors(testMatrix);
        expected.add(testMatrix[8][9]);
        expected.add(testMatrix[9][8]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
    }

}