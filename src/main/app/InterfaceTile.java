package app;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.Random;

import static app.Game.*;

public class InterfaceTile extends StackPane {

    private final int x;
    private final int y;
    private final double offset;

    private final Text text = new Text("");
    private final Polygon border = new Polygon();

    public InterfaceTile(int x, int y, double offset) {
        this.y = y;
        this.x = x;
        this.offset = offset;
    }

    public void drawTile() {
        //формирование шестиугольника
        border.setRotate(90.0);
        border.getPoints().addAll(25.0, 6.25, 37.5, 6.25, 43.75, 18.75, 37.5, 31.25, 25.0, 31.25, 18.75, 18.75);
        //цвет текста, рамки и самой ячейки
        border.setFill(Color.GRAY);
        border.setStroke(Color.BLACK);
        text.setFill(Color.BLACK);
        getChildren().addAll(border, text);
        //размещение ячейки на Pane, offset - сдвиг нечетных рядов вправо
        setTranslateX(offset + x * TILE_SIZE_X);
        setTranslateY(y * TILE_SIZE_Y);
        //обработка кликов по ячейке
        setOnMouseClicked(event -> {
            if (!gameOver)
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (gameMatrix[y][x].hasFlag()) return;
                    if (!firstTileWasClicked)
                        openFirstTile();
                    else open();
                } else if (event.getButton() == MouseButton.SECONDARY) setFlag();
        });
    }

    private void openFirstTile() {
        firstTileWasClicked = true;
        gameMatrix[y][x].setFirstTile(true); //первая нажатая клетка и 6 вокруг нее помечаются, чтобы не поместить в них мину
        for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix)) tile.setFirstTile(true); //и не проиграть на первом ход
        randMines();
        for (MatrixTile tile : mines) {
            gameMatrix[tile.getY()][tile.getX()].setMine();
            for (MatrixTile a : gameMatrix[tile.getY()][tile.getX()].getNeighbors(gameMatrix)) a.incMinesAround();
        }
        open();
        for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix))
            gameField[tile.getY()][tile.getX()].open(); //на первом клике открываются минимум 7 клеток
    }

    private void randMines() {
        //случайное заполнение mines
        Random rnd = new Random(System.currentTimeMillis());
        int y, x; //формирование множества случаных неповторяющихся клеток
        while (mines.size() != INPUT_MINES_NUMBER) {
            y = rnd.nextInt(INPUT_HEIGHT);
            x = rnd.nextInt(INPUT_WIDTH);
            if (!gameMatrix[y][x].isFirstTile())    //мины не помещаются в первую нажатую клетку и вокруг нее
                mines.add(gameMatrix[y][x]);
        }
        //расстановка мин
    }

    private void open() {
        if (gameMatrix[y][x].isOpen()) return; //клик на уже открытую клетку
        gameMatrix[y][x].setOpen();
        if (gameMatrix[y][x].hasFlag()) {     //не выполняется при клике мышкой
            flagsLeft++;   //если открывается сразу несколько клеток, флаги с открывающихся клеток сбрасываются
            flagsLeftField.setText("Flags left: " + flagsLeft);
        }
        if (gameMatrix[y][x].isMine()) { //клик на мину
            gameOver(false);
            return;
        }
        border.setFill(Color.WHITE);
        text.setText(gameMatrix[y][x].getMinesAround().toString());
        tilesBeforeVictory--;
        if (tilesBeforeVictory == 0) gameOver(true);
        if (gameMatrix[y][x].getMinesAround() == 0)
            for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix))
                gameField[tile.getY()][tile.getX()].open();
    }

    private void setFlag() {
        if (gameMatrix[y][x].isOpen()) return; //установка флага на открытую клетку
        if (gameMatrix[y][x].hasFlag()) { //удаление флага
            gameMatrix[y][x].setFlag(false);
            flagsLeft++;
            flagsLeftField.setText("Flags left: " + flagsLeft);
            tilesBeforeVictory++;
            text.setText("");
            return;
        }
        gameMatrix[y][x].setFlag(true);
        tilesBeforeVictory--;
        flagsLeft--;
        flagsLeftField.setText("Flags left: " + flagsLeft);
        text.setText("F");
        if (tilesBeforeVictory == 0) gameOver(true);
    }

    private void gameOver(boolean winOrLoss) {
        gameOver = true;
        if (winOrLoss) {
            flagsLeftField.setText("YOU WON!");
            flagsLeftField.setTextFill(Color.GREEN);
        } else {
            flagsLeftField.setText("YOU LOSE!");
            flagsLeftField.setTextFill(Color.RED);
            for (MatrixTile mine : mines) {
                gameField[mine.getY()][mine.getX()].border.setFill(Color.RED);
                gameField[mine.getY()][mine.getX()].text.setText("X");
            }
        }
    }

}