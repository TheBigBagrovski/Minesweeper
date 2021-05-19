package app;

import app.Game.MatrixTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogicTests {

    Game game;
    MatrixTile[][] testMatrix;

    @BeforeEach
    private void before() {
        game = new Game(10, 10, 1);
        testMatrix = game.getGameMatrix();
    }

    @Test
    public void test_setFlag() {
        testMatrix[0][0].setFlag();
        assertTrue(testMatrix[0][0].hasFlag());
        testMatrix[0][0].setFlag();
        assertFalse(testMatrix[0][0].hasFlag());
    }

    @Test
    public void test_minesAround() {
        game.getMines().add(testMatrix[0][0]);
        game.getMines().add(testMatrix[0][9]);
        game.getMines().add(testMatrix[0][8]);
        game.getMines().add(testMatrix[8][0]);
        game.getMines().add(testMatrix[8][1]);
        game.getMines().add(testMatrix[9][1]);
        game.getMines().addAll(testMatrix[5][5].getNeighbors(testMatrix));
        for (MatrixTile tile : game.getMines()) {
            tile.setMine();
            for (MatrixTile neighbor : tile.getNeighbors(testMatrix))
                neighbor.incMinesAround();
        }
        assertEquals(1, testMatrix[0][1].getMinesAround());
        assertEquals(2, testMatrix[1][8].getMinesAround());
        assertEquals(3, testMatrix[9][0].getMinesAround());
        for (MatrixTile tile : testMatrix[5][5].getNeighbors(testMatrix)) {
            tile.setMine();
        }
        assertEquals(6, testMatrix[5][5].getMinesAround());
    }

    @Test
    public void test_gameOverWin() {
        testMatrix[5][5].open();
        for (MatrixTile mine : game.getMines()) {
            testMatrix[mine.getY()][mine.getX()].setFlag();
        }
        assertTrue(game.isGameOver());
        assertTrue(game.isVictory());
    }

    @Test
    public void test_gameOverLose() {
        testMatrix[0][0].setMine();
        testMatrix[0][0].open();
        assertFalse(game.isVictory());
    }

    @Test
    public void test_getNeighbors() {
        List<MatrixTile> expected = new ArrayList<>();
        List<MatrixTile> actual;
        actual = testMatrix[0][0].getNeighbors(testMatrix);
        expected.add(testMatrix[1][0]);
        expected.add(testMatrix[0][1]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[0][5].getNeighbors(testMatrix);
        expected.add(testMatrix[0][4]);
        expected.add(testMatrix[0][6]);
        expected.add(testMatrix[1][4]);
        expected.add(testMatrix[1][5]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[0][9].getNeighbors(testMatrix);
        expected.add(testMatrix[0][8]);
        expected.add(testMatrix[1][8]);
        expected.add(testMatrix[1][9]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[2][0].getNeighbors(testMatrix);
        expected.add(testMatrix[1][0]);
        expected.add(testMatrix[2][1]);
        expected.add(testMatrix[3][0]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[2][5].getNeighbors(testMatrix);
        expected.add(testMatrix[1][4]);
        expected.add(testMatrix[1][5]);
        expected.add(testMatrix[2][4]);
        expected.add(testMatrix[2][6]);
        expected.add(testMatrix[3][4]);
        expected.add(testMatrix[3][5]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[2][9].getNeighbors(testMatrix);
        expected.add(testMatrix[1][8]);
        expected.add(testMatrix[1][9]);
        expected.add(testMatrix[2][8]);
        expected.add(testMatrix[3][8]);
        expected.add(testMatrix[3][9]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[5][0].getNeighbors(testMatrix);
        expected.add(testMatrix[4][0]);
        expected.add(testMatrix[4][1]);
        expected.add(testMatrix[5][1]);
        expected.add(testMatrix[6][0]);
        expected.add(testMatrix[6][1]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[5][5].getNeighbors(testMatrix);
        expected.add(testMatrix[4][5]);
        expected.add(testMatrix[4][6]);
        expected.add(testMatrix[5][4]);
        expected.add(testMatrix[5][6]);
        expected.add(testMatrix[6][5]);
        expected.add(testMatrix[6][6]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[5][9].getNeighbors(testMatrix);
        expected.add(testMatrix[4][9]);
        expected.add(testMatrix[5][8]);
        expected.add(testMatrix[6][9]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[9][0].getNeighbors(testMatrix);
        expected.add(testMatrix[8][0]);
        expected.add(testMatrix[8][1]);
        expected.add(testMatrix[9][1]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[9][5].getNeighbors(testMatrix);
        expected.add(testMatrix[8][5]);
        expected.add(testMatrix[8][6]);
        expected.add(testMatrix[9][4]);
        expected.add(testMatrix[9][6]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
        actual = testMatrix[9][9].getNeighbors(testMatrix);
        expected.add(testMatrix[8][9]);
        expected.add(testMatrix[9][8]);
        assertEquals(expected, actual);
        actual.clear();
        expected.clear();
    }

}