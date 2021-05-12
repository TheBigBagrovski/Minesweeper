package app;

import app.Game.MatrixTile;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    private static final String[] args = new String[]{""};

    @BeforeAll
    static void starting() {
        Main.main(args);
    }

    @Test
    public void test_setFlag() {
        Game game = new Game(10, 10, 1);
        MatrixTile[][] testMatrix = game.getGameMatrix();
        Interface.InterfaceTile[][] gameField = game.getGameField();
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                gameField[i][j] = new Interface.InterfaceTile(i, j, 0, game);
        testMatrix[0][0].setMine();
        game.getMines().add(testMatrix[0][0]);
        testMatrix[0][1].setFlag();
        assertEquals("F", gameField[0][1].getText());
        testMatrix[0][0].open();
        assertEquals("X", gameField[0][0].getText());
    }

    @Test
    public void test_minesAround() {
        Game game = new Game(10, 10, 1);
        MatrixTile[][] testMatrix = game.getGameMatrix();
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
        Game game = new Game(10, 10, 1);
        MatrixTile[][] testMatrix = game.getGameMatrix();
        Interface.InterfaceTile[][] gameField = game.getGameField();
        testMatrix[0][0].setMine();
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                gameField[i][j] = new Interface.InterfaceTile(i, j, 0, game);
        game.setFirstTileWasClicked(true);
        gameField[0][0].clickSetFlag();
        testMatrix[5][5].open();
        String actualText = game.getFlagsLeftField().getText();
        javafx.scene.paint.Color actualColor = (javafx.scene.paint.Color) game.getFlagsLeftField().getTextFill();
        javafx.scene.paint.Color expectedColor = Color.GREEN;
        String expectedText = "YOU WON!";
        assertEquals(actualText, expectedText);
        assertEquals(actualColor, expectedColor);
    }

    @Test
    public void test_gameOverLose() {
        Game game = new Game(10, 10, 1);
        MatrixTile[][] testMatrix = game.getGameMatrix();
        testMatrix[0][0].setMine();
        game.setFirstTileWasClicked(true);
        testMatrix[0][0].open();
        String actualText = game.getFlagsLeftField().getText();
        javafx.scene.paint.Color actualColor = (javafx.scene.paint.Color) game.getFlagsLeftField().getTextFill();
        javafx.scene.paint.Color expectedColor = Color.RED;
        String expectedText = "YOU LOSE!";
        assertEquals(actualText, expectedText);
        assertEquals(actualColor, expectedColor);
    }

    @Test
    public void test_getNeighbors() {
        List<MatrixTile> expected = new ArrayList<>();
        List<MatrixTile> actual;
        Game game = new Game(10, 10, 10);
        MatrixTile[][] testMatrix = game.getGameMatrix();
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