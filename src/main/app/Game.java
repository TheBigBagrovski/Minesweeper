package app;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.*;

public class Game {

    public static final int TILE_SIZE_X = 25;
    public static final double TILE_SIZE_Y = 18.75;
    public static final double X_OFFSET = 12.5;
    public static Tile[][] matrix;   //игровое поле

    private static int INPUT_WIDTH;
    private static int INPUT_HEIGHT;
    private static int INPUT_MINES_NUMBER;

    private final Stage game = new Stage();
    private static final Set<Tile> mines = new HashSet<>(); //ячейки с минами
    private static int flagsLeft; //количество мин, которые осталось закрыть флагом
    private static final Label flagsLeftField = new Label(); //поле с flagsLeft
    private static double offset; //сдвиг для нечетных рядов на поле
    private static int tilesBeforeVictory; //кол-во клеток, которые надо открыть/поставить флаг до победы
    private static boolean firstTileWasClicked; //проверка того, что была открыта первая клетка
    private static boolean gameOver; //проверка окончания игры

    @FXML
    private TextField fieldWidth_input;

    @FXML
    private TextField fieldHeight_input;

    @FXML
    private TextField minesNumber_input;

    @FXML
    private Label errorField;

    public void clickPlayButton() {
        if (!game.isShowing()) { //одна игра одновременно
            try { //ограничения на ввод: ширина [2..50], высота [2..40], мины [1..(ширина*высота-7)]
                if (fieldHeight_input.getText().isEmpty() || fieldWidth_input.getText().isEmpty() || minesNumber_input.getText().isEmpty())
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
                matrix[y][x] = tile;
                tile.drawTile();
                root.getChildren().add(tile);
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

    public static class Tile extends StackPane {

        private final int x;
        private final int y;
        private boolean isMine;
        private Integer minesAround = 0;
        private boolean isOpen = false;
        private boolean hasFlag = false;
        private boolean firstTile = false;
        private final Text text = new Text("");
        private final Polygon border = new Polygon();

        public Tile(int x, int y) {
            this.y = y;
            this.x = x;
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
                        if (hasFlag) return;
                        if (!firstTileWasClicked)
                            openFirstTile();
                        else open();
                    } else if (event.getButton() == MouseButton.SECONDARY) setFlag();
            });
        }

        public void setMine() {
            this.isMine = true;
        }

        private void incMinesAround() {
            minesAround++;
        }

        public List<Tile> getNeighbors() {
            List<Tile> list = new ArrayList<>();
            if (y == 0) { //если первая строка в матрице
                if (x == 0) { //начало строки
                    list.add(matrix[1][0]);
                    list.add(matrix[0][1]);
                } else if (x == INPUT_WIDTH - 1) { //конец строки
                    list.add(matrix[0][x - 1]);
                    list.add(matrix[1][x - 1]);
                    list.add(matrix[1][x]);
                } else { //остальная часть строки
                    list.add(matrix[0][x - 1]);
                    list.add(matrix[0][x + 1]);
                    list.add(matrix[1][x - 1]);
                    list.add(matrix[1][x]);
                }
            } else if (y % 2 == 0) { //если строка четная
                if (y == INPUT_HEIGHT - 1) { //если последняя строка
                    if (x == 0) {       //если первая клетка в строке
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x + 1]);
                    } else if (x == INPUT_WIDTH - 1) { //последняя клетка в строке
                        list.add(matrix[y - 1][x - 1]);
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                    } else {            //остальные клетки
                        list.add(matrix[y - 1][x - 1]);
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y][x + 1]);
                    }
                } else {   //если не последняя строка
                    if (x == 0) {
                        list.add(matrix[y - 1][0]);
                        list.add(matrix[y][1]);
                        list.add(matrix[y + 1][0]);
                    } else if (x == INPUT_WIDTH - 1) {
                        list.add(matrix[y - 1][x - 1]);
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y + 1][x - 1]);
                        list.add(matrix[y + 1][x]);
                    } else {
                        list.add(matrix[y - 1][x - 1]);
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y][x + 1]);
                        list.add(matrix[y + 1][x - 1]);
                        list.add(matrix[y + 1][x]);
                    }
                }
            } else {
                if (y == INPUT_HEIGHT - 1) {
                    if (x == 0) {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y - 1][x + 1]);
                        list.add(matrix[y][x + 1]);
                    } else if (x == INPUT_WIDTH - 1) {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                    } else {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y - 1][x + 1]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y][x + 1]);
                    }
                } else {
                    if (x == 0) {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y - 1][x + 1]);
                        list.add(matrix[y][x + 1]);
                        list.add(matrix[y + 1][x]);
                        list.add(matrix[y + 1][x + 1]);
                    } else if (x == INPUT_WIDTH - 1) {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y + 1][x]);
                    } else {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y - 1][x + 1]);
                        list.add(matrix[y][x - 1]);
                        list.add(matrix[y][x + 1]);
                        list.add(matrix[y + 1][x]);
                        list.add(matrix[y + 1][x + 1]);
                    }
                }
            }
            return list;
        } //определение соседей клетки

        private void openFirstTile() {
            firstTileWasClicked = true;
            firstTile = true; //первая нажатая клетка и 6 вокруг нее помечаются, чтобы не поместить в них мину
            for (Tile tile : getNeighbors()) tile.firstTile = true;    //и не проиграть на первом ходу
            //случайное заполнение mines
            Random rnd = new Random(System.currentTimeMillis());
            int y, x; //формирование множества случаных неповторяющихся клеток
            while (mines.size() != INPUT_MINES_NUMBER) {
                y = rnd.nextInt(INPUT_HEIGHT);
                x = rnd.nextInt(INPUT_WIDTH);
                if (!matrix[y][x].firstTile)    //мины не помещаются в первую нажатую клетку и вокруг нее
                    mines.add(matrix[y][x]);
            }
            //расстановка мин
            for (Tile tile : mines) {
                matrix[tile.y][tile.x].setMine();
                for (Tile a : matrix[tile.y][tile.x].getNeighbors()) a.incMinesAround();
            }
            open();
            for (Tile tile : getNeighbors()) tile.open(); //на первом клике открываются минимум 7 клеток
        }

        private void open() {
            if (isOpen) return; //клик на уже открытую клетку
            isOpen = true;
            if (hasFlag) {     //не выполняется при клике мышкой
                flagsLeft++;   //если открывается сразу несколько клеток, флаги с открывающихся клеток сбрасываются
                flagsLeftField.setText("Flags left: " + flagsLeft);
            }
            if (isMine) { //клик на мину
                gameOver(false);
                return;
            }
            border.setFill(Color.WHITE);
            text.setText(minesAround.toString());
            tilesBeforeVictory--;
            if (tilesBeforeVictory == 0) gameOver(true);
            if (minesAround == 0) getNeighbors().forEach(Tile::open);
        }

        private void gameOver(boolean winOrLoss) {
            gameOver = true;
            if (winOrLoss) {
                flagsLeftField.setText("YOU WON!");
                flagsLeftField.setTextFill(Color.GREEN);
            } else {
                flagsLeftField.setText("YOU LOSE!");
                flagsLeftField.setTextFill(Color.RED);
                for (Tile mine : mines) {
                    mine.border.setFill(Color.RED);
                    mine.text.setText("X");
                }
            }
        }

        private void setFlag() {
            if (isOpen) return; //установка флага на открытую клетку
            if (hasFlag) { //удаление флага
                hasFlag = false;
                flagsLeft++;
                flagsLeftField.setText("Flags left: " + flagsLeft);
                tilesBeforeVictory++;
                text.setText("");
                return;
            }
            hasFlag = true;
            tilesBeforeVictory--;
            flagsLeft--;
            flagsLeftField.setText("Flags left: " + flagsLeft);
            text.setText("F");
            if (tilesBeforeVictory == 0) gameOver(true);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj instanceof Tile) {
                Tile other = (Tile) obj;
                return this.x == other.x && this.y == other.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

    }

}