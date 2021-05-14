package app;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Interface {

    public static final int TILE_SIZE_X = 25;
    public static final double TILE_SIZE_Y = 18.75;
    public static final double X_OFFSET = 12.5;

    private final Game game;
    private final Label flagsLeftField = new Label(); //поле с flagsLeft
    private final InterfaceTile[][] gameField;
    private final Pane root;

    public Pane getRoot() {
        return root;
    }

    public InterfaceTile[][] getGameField() {
        return gameField;
    }

    public Label getFlagsLeftField() {
        return flagsLeftField;
    }

    public Interface(int height, int width, Game game) {
        this.game = game;
        gameField = new InterfaceTile[height][width];
        root = new Pane();
        double offset;
        for (int y = 0; y < height; y++) {
            if (y % 2 != 0) {
                offset = X_OFFSET;
            } else offset = 0;
            for (int x = 0; x < width; x++) {
                Interface.InterfaceTile tile = new Interface.InterfaceTile(x, y, offset, this);
                gameField[y][x] = tile;
                root.getChildren().add(tile);
            }
        }
        root.setPrefSize(17 + width * TILE_SIZE_X, 10 + height * TILE_SIZE_Y + 20);
        flagsLeftField.setText("Flags left: " + game.getFlagsLeft());
        flagsLeftField.setTextFill(Color.BLACK);
        flagsLeftField.setTranslateX(width * TILE_SIZE_X / 2.0 - 23);
        flagsLeftField.setTranslateY(height * TILE_SIZE_Y + 10);
        root.getChildren().add(flagsLeftField);
    }

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

    public class InterfaceTile extends StackPane {

        private final int x;
        private final int y;
        private final Interface parentInterface;
        private final Text text = new Text("");
        private final Polygon border = new Polygon();
        private final HitBox hitBox;

        public HitBox getHitBox() {
            return hitBox;
        }

        public String getText() {
            return text.getText();
        }

        public InterfaceTile(int x, int y, double offset, Interface parentInterface) {
            this.y = y;
            this.x = x;
            this.parentInterface = parentInterface;
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
            //создание хитбокса
            hitBox = new HitBox();
            hitBox.setActive();
            this.getChildren().add(hitBox);
        }


        private void clickOpen() {
            game.getGameMatrix()[y][x].open(parentInterface);
        }

        public void clickSetFlag() {
            game.getGameMatrix()[y][x].setFlag(parentInterface);
        }

        public void drawMine() {
            border.setFill(Color.RED);
            text.setText("X");
            hitBox.setInactive();
        }

        public void drawOpenTile() {
            border.setFill(Color.WHITE);
            text.setText(game.getGameMatrix()[this.y][this.x].getMinesAround().toString());
            hitBox.setInactive();
        }

        public void drawSetFlag() {
            text.setText("F");
            border.setFill(Color.GRAY);
            hitBox.setInactive();
        }

        public void drawRemoveFlag() {
            text.setText("");
            hitBox.setActive();
        }

        public class HitBox extends Rectangle {

            public HitBox() {
                setWidth(TILE_SIZE_X);
                setHeight(TILE_SIZE_Y + 3);
                setActive();
                setFill(Color.TRANSPARENT);
                //обработка кликов по ячейке
                setOnMouseClicked(event -> {
                    if (!game.isGameOver())
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (game.getGameMatrix()[y][x].hasFlag()) return;
                            clickOpen();
                        } else if (event.getButton() == MouseButton.SECONDARY) clickSetFlag();
                });
            }

            public void setActive() {
                setOnMouseEntered(event -> border.setFill(Color.WHITE));
                setOnMouseExited(event -> border.setFill(Color.GRAY));
            }

            public void setInactive() {
                setOnMouseEntered(event -> {
                });
                setOnMouseExited(event -> {
                });
            }

        }

    }

}