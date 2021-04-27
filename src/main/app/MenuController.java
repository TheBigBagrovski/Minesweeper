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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuController {

    public static final int TILE_SIZE_X = 25;
    public static final double TILE_SIZE_Y = 18.75;
    public static final double X_OFFSET = 12.5;

    public static int INPUT_WIDTH;
    public static int INPUT_HEIGHT;
    public static int INPUT_MINES_NUMBER;

    private final Stage game = new Stage();

    public static Tile[][] matrix;   //игровое поле
    public static Set<Tile> mines = new HashSet<>(); //ячейки с минами
    public static int flagsLeft; //количество мин, которые осталось закрыть флагом
    public static Label flagsLeftField = new Label(); //поле с flagsLeft
    public static double offset; //сдвиг для нечетных рядов на поле
    public static int tilesBeforeVictory; //кол-во клеток, которые надо открыть/поставить флаг до победы
    public static boolean firstTileWasClicked; //проверка того, что была открыта первая клетка
    public static boolean gameOver; //проверка окончания игры

    @FXML
    private TextField fieldWidth_input;

    @FXML
    private TextField fieldHeight_input;

    @FXML
    private TextField minesNumber_input;

    @FXML
    private Label errorField;

    public void clickPlayButton() {
        if (!game.isShowing()) { //1 игра одновременно
            try { //ограничения на ввод: ширина [2..50], высота [2..40], мины [1..(ширина*высота-7)]
                if (fieldHeight_input.getText().equals("") || fieldWidth_input.getText().equals("") || minesNumber_input.getText().equals(""))
                    throw new IllegalStateException("Error: fill all fields");
                INPUT_WIDTH = Integer.parseInt(fieldWidth_input.getText());
                INPUT_HEIGHT = Integer.parseInt(fieldHeight_input.getText());
                INPUT_MINES_NUMBER = Integer.parseInt(minesNumber_input.getText());
                if (INPUT_WIDTH < 2 || INPUT_HEIGHT < 2 || INPUT_MINES_NUMBER < 1 || INPUT_MINES_NUMBER > INPUT_HEIGHT * INPUT_WIDTH - 7)
                    throw new NumberFormatException();
                if (INPUT_WIDTH > 50 || INPUT_HEIGHT > 40)
                    throw new IllegalStateException("Error: max field size is 40x50");
            } catch (NumberFormatException e) {
                errorField.setText("Error: wrong input");
                return;
            } catch (IllegalStateException e) {
                errorField.setText(e.getMessage());
                return;
            }
            startGame(); //если все проверки пройдены запускается игра
        } else errorField.setText("Game has already started");
    }

    private void startGame() {

        Pane root = new Pane();
        root.setPrefSize(17 + INPUT_WIDTH * TILE_SIZE_X, 10 + INPUT_HEIGHT * TILE_SIZE_Y + 20);
        matrix = new Tile[INPUT_HEIGHT][INPUT_WIDTH];
        mines.clear();
        gameOver = false;
        firstTileWasClicked = false;
        tilesBeforeVictory = INPUT_WIDTH * INPUT_HEIGHT;
        flagsLeft = INPUT_MINES_NUMBER;
        flagsLeftField.setTextFill(Color.BLACK);
        flagsLeftField.setText("Flags left: " + flagsLeft);
        //формирование пустой матрицы и построение поля из ячеек
        for (int y = 0; y < INPUT_HEIGHT; y++) {
            if (y % 2 != 0) {
                offset = X_OFFSET;
            } else offset = 0;
            for (int x = 0; x < INPUT_WIDTH; x++) {
                Tile tile = new Tile(x, y);
                root.getChildren().add(tile);
                matrix[y][x] = tile;
            }
        }
        //добавление поля с оставшимися флагами
        flagsLeftField.setTranslateX(INPUT_WIDTH * TILE_SIZE_X / 2.0 - 23);
        flagsLeftField.setTranslateY(INPUT_HEIGHT * TILE_SIZE_Y + 10);
        root.getChildren().add(flagsLeftField);
        //значок и название окна
        game.setTitle("Minesweeper");
        InputStream iconStream = getClass().getResourceAsStream("/icon.png");
        Image icon = new Image(iconStream);
        game.getIcons().add(icon);
        //очистка поля для вывода ошибок в настройках при успешном запуске игры, запуск игры
        errorField.setText("");
        game.setScene(new Scene(root));
        game.show();

    }

}