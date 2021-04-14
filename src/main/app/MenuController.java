package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MenuController {

    @FXML
    private TextField fieldWidth_input;

    @FXML
    private TextField fieldHeight_input;

    @FXML
    private TextField minesNumber_input;

    @FXML
    private Label errorField;

    public void clickPlayButton(javafx.event.ActionEvent event) throws IOException {
        try {
            if (fieldHeight_input.getText().equals("") || fieldWidth_input.getText().equals("") || minesNumber_input.getText().equals(""))
                throw new IllegalStateException("Error: fill all fields");
            int inputWidth = Integer.parseInt(fieldWidth_input.getText());
            int inputHeight = Integer.parseInt(fieldHeight_input.getText());
            int inputMinesNumber = Integer.parseInt(minesNumber_input.getText());
            if (inputWidth < 2 || inputHeight < 2 || inputMinesNumber < 1 || inputMinesNumber >= inputHeight * inputWidth) throw new NumberFormatException();
            if (inputWidth > 50 || inputHeight > 50) throw new IllegalStateException("Error: max field size is 50x50");

            GameField gameField = new GameField(inputWidth, inputHeight, inputMinesNumber);

            Stage game = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
            game.setScene(new Scene(root));
            game.setTitle("Minesweeper");
            InputStream iconStream = getClass().getResourceAsStream("/icon.png");
            Image icon = new Image(iconStream);
            game.getIcons().add(icon);
            game.show();
        } catch (NumberFormatException e) {
            errorField.setText("Error: wrong input");
        } catch (IllegalStateException e) {
            errorField.setText(e.getMessage());
        }
    }

}