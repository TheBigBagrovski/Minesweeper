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

import static app.Game.*;

public class Menu {

    @FXML
    private TextField inputField_width;

    @FXML
    private TextField inputField_height;

    @FXML
    private TextField inputField_mines;

    @FXML
    private Label errorField;

    public void clickTextField() {
        inputField_width.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!inputField_width.getText().matches("\\d*")) {
                    inputField_width.setText("");
                }
            }
        });
        inputField_height.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!inputField_height.getText().matches("\\d*")) {
                    inputField_height.setText("");
                }
            }
        });
        inputField_mines.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!inputField_mines.getText().matches("\\d*")) {
                    inputField_mines.setText("");
                }
            }
        });
    }

    public void clickPlayButton() {
        if (inputField_width.getText().isEmpty() || inputField_height.getText().isEmpty() || inputField_mines.getText().isEmpty()) {
            errorField.setText("Error: fill all fields");
            return;
        }
        int inputHeight = Integer.parseInt(inputField_height.getText());
        int inputWidth = Integer.parseInt(inputField_width.getText());
        int inputMinesNumber = Integer.parseInt(inputField_mines.getText());
        if (inputHeight < 2 || inputWidth < 2) errorField.setText("Error: min field size is 2x2");
        else if (inputWidth > 50 || inputHeight > 40) errorField.setText("Error: max field size is 40x50");
        else if (inputMinesNumber < 1 || inputMinesNumber > inputWidth * inputHeight - 7)
            errorField.setText("Error: wrong mines number");
        else startGame(inputHeight, inputWidth, inputMinesNumber);
    }

    private void startGame(int height, int width, int minesNumber) {
        Game game = new Game(height, width, minesNumber);
        Stage stage = new Stage();
        Pane root = new Pane();
        root.setPrefSize(17 + width * TILE_SIZE_X, 10 + height * TILE_SIZE_Y + 20);
        game.getFlagsLeftField().setText("Flags left: " + game.getFlagsLeft());
        game.getFlagsLeftField().setTextFill(Color.BLACK);
        double offset;
        for (int y = 0; y < height; y++) {
            if (y % 2 != 0) {
                offset = X_OFFSET;
            } else offset = 0;
            for (int x = 0; x < width; x++) {
                Interface.InterfaceTile tile = new Interface.InterfaceTile(x, y, offset, game);
                game.getGameField()[y][x] = tile;
                root.getChildren().add(tile);
            }
        }
        game.getFlagsLeftField().setTranslateX(width * TILE_SIZE_X / 2.0 - 23);
        game.getFlagsLeftField().setTranslateY(height * TILE_SIZE_Y + 10);
        root.getChildren().add(game.getFlagsLeftField());
        //значок и название окна
        stage.setTitle("Minesweeper");
        InputStream iconStream = getClass().getResourceAsStream("/icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        stage.getIcons().add(icon);
        //очистка поля для вывода ошибок в настройках при успешном запуске игры, запуск игры
        errorField.setText("");
        stage.setScene(new Scene(root));
        stage.show();
    }

}