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

    public void drawVictory() {
        flagsLeftField.setText("YOU WON!");
        flagsLeftField.setTextFill(Color.GREEN);
    }

    public void drawLoss() {
        flagsLeftField.setText("YOU LOSE!");
        flagsLeftField.setTextFill(Color.RED);
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[0].length; j++) {
                if (!game.getGameMatrix()[i][j].hasFlag() && !game.getGameMatrix()[i][j].isOpen())
                    gameField[i][j].getHitBox().setInactive();
            }
        }
        for (Game.MatrixTile mine : game.getMines()) {
            gameField[mine.getY()][mine.getX()].drawTile(InterfaceTile.TileStatus.MINE);
        }
    }

    public static class InterfaceTile extends StackPane {

        private final int x;
        private final int y;
        private final Game game;
        private final Interface parentInterface;
        private final Text text = new Text("");
        private final Polygon border = new Polygon();
        private final HitBox hitBox = new HitBox();

        private enum TileStatus {
            EMPTY,
            OPEN,
            FLAG,
            MINE
        }

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
            this.game = parentInterface.game;
            //формирование шестиугольника
            border.setRotate(90.0);
            border.getPoints().addAll(25.0, 6.25, 37.5, 6.25, 43.75, 18.75, 37.5, 31.25, 25.0, 31.25, 18.75, 18.75);
            //цвет текста, рамки и самой ячейки
            border.setStroke(Color.BLACK);
            text.setFill(Color.BLACK);
            getChildren().addAll(border, text);
            //размещение ячейки на Pane, offset - сдвиг нечетных рядов вправо
            setTranslateX(offset + x * TILE_SIZE_X);
            setTranslateY(y * TILE_SIZE_Y);
            //создание хитбокса
            hitBox.setActive();
            this.getChildren().add(hitBox);
            drawTile(TileStatus.EMPTY);
        }

        public void clickOpen() {
            game.getGameMatrix()[y][x].open();
            drawAllTiles();
            parentInterface.getFlagsLeftField().setText("Flags left: " + game.getFlagsLeft());
            checkEndGame();
        }

        public void clickSetFlag() {
            if (game.getGameMatrix()[y][x].hasFlag()) drawTile(TileStatus.EMPTY);
            else drawTile(TileStatus.FLAG);
            game.getGameMatrix()[y][x].setFlag();
            parentInterface.getFlagsLeftField().setText("Flags left: " + game.getFlagsLeft());
            checkEndGame();
        }

        public void checkEndGame() {
            if (game.isGameOver()) {
                if (game.isVictory()) parentInterface.drawVictory();
                else parentInterface.drawLoss();
            }
        }

        public void drawAllTiles() {
            for (int i = 0; i < parentInterface.getGameField().length; i++) {
                for (int j = 0; j < parentInterface.getGameField()[0].length; j++) {
                    Game.MatrixTile[][] matrix = game.getGameMatrix();
                    TileStatus status;
                    if (matrix[i][j].hasFlag()) status = TileStatus.FLAG;
                    else if (matrix[i][j].isOpen()) status = TileStatus.OPEN;
                    else status = TileStatus.EMPTY;
                    parentInterface.getGameField()[i][j].drawTile(status);
                }
            }
        }

        public void drawTile(TileStatus status) {
            switch (status) {
                case EMPTY -> {
                    text.setText("");
                    border.setFill(Color.GRAY);
                    hitBox.setActive();
                }
                case OPEN -> {
                    border.setFill(Color.WHITE);
                    text.setText(game.getGameMatrix()[this.y][this.x].getMinesAround().toString());
                    hitBox.setInactive();
                }
                case FLAG -> {
                    text.setText("F");
                    border.setFill(Color.GRAY);
                    hitBox.setInactive();
                }
                case MINE -> {
                    border.setFill(Color.RED);
                    text.setText("X");
                    hitBox.setInactive();
                }
            }
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
                        } else if (event.getButton() == MouseButton.SECONDARY && !game.getGameMatrix()[y][x].isOpen()) clickSetFlag();
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