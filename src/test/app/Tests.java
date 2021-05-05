package app;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    //private final String[] args = new String[] {""};
    private final List<Game.Tile> expected = new ArrayList<>();
    private List<Game.Tile> actual = new ArrayList<>();

    private void clearExpectedActualLists() {
        actual.clear();
        expected.clear();
    }

    @Test
    public void test_getNeighbors() {
        Game.Tile[][] matrix = new Game.Tile[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = new Game.Tile(j, i);
            }
        }
        clearExpectedActualLists();
        actual = matrix[0][0].getNeighbors();
        expected.add(matrix[1][0]);
        expected.add(matrix[0][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[0][5].getNeighbors();
        expected.add(matrix[0][4]);
        expected.add(matrix[0][6]);
        expected.add(matrix[1][4]);
        expected.add(matrix[1][5]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[0][9].getNeighbors();
        expected.add(matrix[0][8]);
        expected.add(matrix[1][8]);
        expected.add(matrix[1][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[2][0].getNeighbors();
        expected.add(matrix[1][0]);
        expected.add(matrix[2][1]);
        expected.add(matrix[3][0]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[2][5].getNeighbors();
        expected.add(matrix[1][4]);
        expected.add(matrix[1][5]);
        expected.add(matrix[2][4]);
        expected.add(matrix[2][6]);
        expected.add(matrix[3][4]);
        expected.add(matrix[3][5]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[2][9].getNeighbors();
        expected.add(matrix[1][8]);
        expected.add(matrix[1][9]);
        expected.add(matrix[2][8]);
        expected.add(matrix[3][8]);
        expected.add(matrix[3][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[5][0].getNeighbors();
        expected.add(matrix[4][0]);
        expected.add(matrix[4][1]);
        expected.add(matrix[5][1]);
        expected.add(matrix[6][0]);
        expected.add(matrix[6][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[5][5].getNeighbors();
        expected.add(matrix[4][5]);
        expected.add(matrix[4][6]);
        expected.add(matrix[5][4]);
        expected.add(matrix[5][6]);
        expected.add(matrix[6][5]);
        expected.add(matrix[6][6]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[5][9].getNeighbors();
        expected.add(matrix[4][9]);
        expected.add(matrix[5][8]);
        expected.add(matrix[6][9]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[9][0].getNeighbors();
        expected.add(matrix[8][0]);
        expected.add(matrix[8][1]);
        expected.add(matrix[9][1]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[9][5].getNeighbors();
        expected.add(matrix[8][5]);
        expected.add(matrix[8][6]);
        expected.add(matrix[9][4]);
        expected.add(matrix[9][6]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
        actual = matrix[9][9].getNeighbors();
        expected.add(matrix[8][9]);
        expected.add(matrix[9][8]);
        assertEquals(expected, actual);
        clearExpectedActualLists();
    }

}