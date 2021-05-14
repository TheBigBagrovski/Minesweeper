package app;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class Menu {

    @FXML
    private TextField inputField_width;

    @FXML
    private TextField inputField_height;

    @FXML
    private TextField inputField_mines;

    @FXML
    private Label errorField;

    private void setValidator(TextField textField) {
        textField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                if (!textField.getText().matches("\\d*")) {
                    textField.setText("");
                }
            }
        });
    }

    public void clickTextField() {
        setValidator(inputField_height);
        setValidator(inputField_width);
        setValidator(inputField_mines);
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
        Interface gameInterface = new Interface(height, width, game);
        Stage stage = new Stage();
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
        stage.setScene(new Scene(gameInterface.getRoot()));
        stage.show();
    }

}