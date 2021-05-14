package app;

import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.api.FxAssert.verifyThat;

public class InterfaceTests extends TestFXBase {

    final String PLAY_BUTTON_ID = "#playButton";
    final String ERROR_FIELD_ID = "#errorField";
    final String INPUT_WIDTH_ID = "#inputField_width";
    final String INPUT_HEIGHT_ID = "#inputField_height";
    final String INPUT_MINES_ID = "#inputField_mines";
    final String ERROR_EMPTY_FIELDS = "Error: fill all fields";
    final String ERROR_MIN_SIZE = "Error: min field size is 2x2";
    final String ERROR_MAX_SIZE = "Error: max field size is 40x50";
    final String ERROR_MINES_NUMBER = "Error: wrong mines number";
    final String INVALID_SYMBOLS = "symbols";
    final String INVALID_BIG_NUMBER = "100";
    final String INVALID_SMALL_NUMBER = "0";
    final String VALID_NUMBER = "10";

    private void clearInputs() {
        clickOn(INPUT_HEIGHT_ID).eraseText(7);
        clickOn(INPUT_WIDTH_ID).eraseText(7);
        clickOn(INPUT_MINES_ID).eraseText(7);
    }

    @Test
    public void clickOnNonexistentButton() {
        assertThrows(FxRobotException.class, () -> clickOn("#someButton"));
    }

    @Test
    public void emptyFields() {
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
           String text = label.getText();
           return text.contains(ERROR_EMPTY_FIELDS);
        });
    }

    @Test
    public void wrongSymbols() {
        clickOn(INPUT_WIDTH_ID).write(INVALID_SYMBOLS);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_EMPTY_FIELDS);
        });
        clickOn(INPUT_HEIGHT_ID).write(INVALID_SYMBOLS);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_EMPTY_FIELDS);
        });
        clickOn(INPUT_MINES_ID).write(INVALID_SYMBOLS);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_EMPTY_FIELDS);
        });
    }

    @Test
    public void wrongNumbers() {
        clickOn(INPUT_WIDTH_ID).write(VALID_NUMBER);
        clickOn(INPUT_MINES_ID).write(VALID_NUMBER);
        clickOn(INPUT_HEIGHT_ID).write(INVALID_BIG_NUMBER);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_MAX_SIZE);
        });
        clearInputs();
        clickOn(INPUT_HEIGHT_ID).write(VALID_NUMBER);
        clickOn(INPUT_MINES_ID).write(VALID_NUMBER);
        clickOn(INPUT_WIDTH_ID).write(INVALID_SMALL_NUMBER);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_MIN_SIZE);
        });
        clearInputs();
        clickOn(INPUT_WIDTH_ID).write(VALID_NUMBER);
        clickOn(INPUT_HEIGHT_ID).write(VALID_NUMBER);
        clickOn(INPUT_MINES_ID).write(INVALID_SMALL_NUMBER);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_MINES_NUMBER);
        });
        clearInputs();
        clickOn(INPUT_WIDTH_ID).write(VALID_NUMBER);
        clickOn(INPUT_HEIGHT_ID).write(VALID_NUMBER);
        clickOn(INPUT_MINES_ID).write(INVALID_BIG_NUMBER);
        clickOn(PLAY_BUTTON_ID);
        verifyThat(ERROR_FIELD_ID, (Label label) -> {
            String text = label.getText();
            return text.contains(ERROR_MINES_NUMBER);
        });
    }
//
//    @Test
//    public void test() throws Exception {
//        setUpClass();
//        clickOn(INPUT_WIDTH_ID).write(VALID_NUMBER);
//        clickOn(INPUT_HEIGHT_ID).write(VALID_NUMBER);
//        clickOn(INPUT_MINES_ID).write(VALID_NUMBER);
//        clickOn(PLAY_BUTTON_ID);
//        sleep(500);
//    }

}