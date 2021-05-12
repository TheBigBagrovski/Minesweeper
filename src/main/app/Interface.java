package app;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Interface {

    public static final int TILE_SIZE_X = 25;
    public static final double TILE_SIZE_Y = 18.75;
    public static final double X_OFFSET = 12.5;

    public static void drawFlagsLeft(int a, Label flagsLeftField, int flagsLeft) {
        switch (a) {
            case 0 -> flagsLeftField.setText("Flags left: " + flagsLeft);
            case 1 -> {
                flagsLeftField.setText("YOU WON!");
                flagsLeftField.setTextFill(Color.GREEN);
            }
            case 2 -> {
                flagsLeftField.setText("YOU LOSE!");
                flagsLeftField.setTextFill(Color.RED);
            }
        }
    }

    public static class InterfaceTile extends StackPane {

        private final int x;
        private final int y;

        private final Text text = new Text("");
        private final Polygon border = new Polygon();
        private final Game game;

        public String getText() {
            return text.getText();
        }

        public InterfaceTile(int x, int y, double offset, Game game) {
            this.y = y;
            this.x = x;
            this.game = game;
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
                if (!game.isGameOver())
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (game.getGameMatrix()[y][x].hasFlag()) return;
                        clickOpen();
                    } else if (event.getButton() == MouseButton.SECONDARY) clickSetFlag();
            });
        }

        private void clickOpen() {
            game.getGameMatrix()[y][x].open();
        }

        public void clickSetFlag() {
            game.getGameMatrix()[y][x].setFlag();
        }

        public void drawOpenTile(Integer minesAround) {
            border.setFill(Color.WHITE);
            text.setText(minesAround.toString());
        }

        public void drawMine() {
            this.border.setFill(Color.RED);
            this.text.setText("X");
        }

        public void drawSetFlag() {
            text.setText("F");
        }

        public void drawRemoveFlag() {
            text.setText("");
        }

    }

}