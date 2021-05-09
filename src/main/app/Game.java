package app;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class Game {

    public static final int TILE_SIZE_X = 25;
    public static final double TILE_SIZE_Y = 18.75;
    public static final double X_OFFSET = 12.5;

    public static int INPUT_WIDTH;
    public static int INPUT_HEIGHT;
    public static int INPUT_MINES_NUMBER;

    public static MatrixTile[][] gameMatrix;   //игровое поле
    public static InterfaceTile[][] gameField;

    private final Stage game = new Stage();
    public static final Set<MatrixTile> mines = new HashSet<>(); //ячейки с минами
    public static int flagsLeft; //количество мин, которые осталось закрыть флагом
    public static final Label flagsLeftField = new Label(); //поле с flagsLeft
    public static int tilesBeforeVictory; //кол-во клеток, которые надо открыть/поставить флаг до победы
    public static boolean firstTileWasClicked; //проверка того, что была открыта первая клетка
    public static boolean gameOver; //проверка окончания игры

    @FXML
    public static TextField fieldWidth_input = new TextField();

    @FXML
    public static TextField fieldHeight_input = new TextField();

    @FXML
    public static TextField minesNumber_input = new TextField();

    @FXML
    private Label errorField;

    public void clickWidthTextField() {
        fieldWidth_input.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!fieldWidth_input.getText().matches("^([2-9]|[1-4][\\d]|50)$")) {
                    fieldWidth_input.setText("");
                }
            }
        });
    }

    public void clickHeightTextField() {
        fieldHeight_input.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!fieldHeight_input.getText().matches("^([2-9]|[1-3][\\d]|40)$")) {
                    fieldHeight_input.setText("");
                }
            }
        });
    }

    public void clickPlayButton() {
        if (!game.isShowing()) { //одна игра одновременно
            if (fieldWidth_input.getText().isEmpty() || fieldHeight_input.getText().isEmpty()) {
                errorField.setText("Error: fill all fields");
                return;
            }
            INPUT_HEIGHT = Integer.parseInt(fieldHeight_input.getText());
            INPUT_WIDTH = Integer.parseInt(fieldWidth_input.getText());
            int minesNum;
            try {
                minesNum = Integer.parseInt(minesNumber_input.getText());
            } catch (NumberFormatException e) {
                errorField.setText("Error: wrong input");
                return;
            }
            INPUT_MINES_NUMBER = minesNum;
            if (minesNum < 1 || minesNum > INPUT_WIDTH * INPUT_HEIGHT - 7)
                errorField.setText("Error: wrong mines number");
            else startGame();
        } else errorField.setText("Game has already started");
    }

    private void startGame() {
        Pane root = new Pane();
        root.setPrefSize(17 + INPUT_WIDTH * TILE_SIZE_X, 10 + INPUT_HEIGHT * TILE_SIZE_Y + 20);
        gameMatrix = new MatrixTile[INPUT_HEIGHT][INPUT_WIDTH];
        gameField = new InterfaceTile[INPUT_HEIGHT][INPUT_WIDTH];
        mines.clear();
        gameOver = false;
        firstTileWasClicked = false;
        tilesBeforeVictory = INPUT_WIDTH * INPUT_HEIGHT;
        flagsLeft = INPUT_MINES_NUMBER;
        flagsLeftField.setTextFill(Color.BLACK);
        flagsLeftField.setText("Flags left: " + flagsLeft);
        //формирование пустой матрицы и построение поля из ячеек
        double offset;
        for (int y = 0; y < INPUT_HEIGHT; y++) {
            if (y % 2 != 0) {
                offset = X_OFFSET;
            } else offset = 0;
            for (int x = 0; x < INPUT_WIDTH; x++) {
                gameMatrix[y][x] = new MatrixTile(x, y);
                InterfaceTile tile = new InterfaceTile(x, y, offset);
                gameField[y][x] = tile;
                root.getChildren().add(tile);
                gameField[y][x].drawTile();
            }
        }
        //добавление поля с оставшимися флагами
        flagsLeftField.setTranslateX(INPUT_WIDTH * TILE_SIZE_X / 2.0 - 23);
        flagsLeftField.setTranslateY(INPUT_HEIGHT * TILE_SIZE_Y + 10);
        root.getChildren().add(flagsLeftField);
        //значок и название окна
        game.setTitle("Minesweeper");
        InputStream iconStream = getClass().getResourceAsStream("/icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        game.getIcons().add(icon);
        //очистка поля для вывода ошибок в настройках при успешном запуске игры, запуск игры
        errorField.setText("");
        game.setScene(new Scene(root));
        game.show();
    }

}