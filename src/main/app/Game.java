package app;

import java.util.*;

public class Game {

    private final int inputWidth;
    private final int inputHeight;
    private final int inputMinesNumber;

    private final MatrixTile[][] gameMatrix;   //игровое поле

    private final Set<MatrixTile> mines = new HashSet<>(); //ячейки с минами
    private int flagsLeft; //количество мин, которые осталось закрыть флагом
    private int tilesBeforeVictory; //кол-во клеток, которые надо открыть/поставить флаг до победы
    private boolean firstTileWasClicked; //проверка того, что была открыта первая клетка
    private boolean gameOver; //проверка окончания игры
    private boolean victory;

    public boolean isVictory() {
        return victory;
    }

    public Set<MatrixTile> getMines() {
        return mines;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isFirstTileWasClicked() {
        return firstTileWasClicked;
    }

    public MatrixTile[][] getGameMatrix() {
        return gameMatrix;
    }

    public int getFlagsLeft() {
        return flagsLeft;
    }

    public Game(int inputHeight, int inputWidth, int inputMinesNumber) {
        this.inputHeight = inputHeight;
        this.inputWidth = inputWidth;
        this.inputMinesNumber = inputMinesNumber;

        tilesBeforeVictory = inputWidth * inputHeight;
        flagsLeft = inputMinesNumber;
        gameMatrix = new MatrixTile[inputHeight][inputWidth];
        for (int y = 0; y < inputHeight; y++) {
            for (int x = 0; x < inputWidth; x++) {
                gameMatrix[y][x] = new MatrixTile(x, y);
            }
        }
    }

    public void endGame(boolean winOrLoss) {
        gameOver = true;
        victory = winOrLoss;
    }

    public class MatrixTile {

        private final int x;
        private final int y;

        private boolean isMine;
        private Integer minesAround = 0;
        private boolean isOpen;
        private boolean hasFlag;
        private boolean firstTile;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isMine() {
            return isMine;
        }

        public Integer getMinesAround() {
            return minesAround;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setMine() {
            isMine = true;
        }

        public boolean hasFlag() {
            return hasFlag;
        }

        public MatrixTile(int x, int y) {
            this.y = y;
            this.x = x;
        }

        public void openFirstTile() {
            firstTileWasClicked = true;
            gameMatrix[y][x].firstTile = true; //первая нажатая клетка и 6 вокруг нее помечаются, чтобы не поместить в них мину
            for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix))
                tile.firstTile = true; //и не проиграть на первом ход
            //случайное заполнение mines
            Random rnd = new Random(System.currentTimeMillis());
            int a, b; //формирование множества случаных неповторяющихся клеток
            while (mines.size() != inputMinesNumber) {
                a = rnd.nextInt(inputHeight);
                b = rnd.nextInt(inputWidth);
                if (!gameMatrix[a][b].firstTile)    //мины не помещаются в первую нажатую клетку и вокруг нее
                    mines.add(gameMatrix[a][b]);
            }
            for (MatrixTile tile : mines) {
                gameMatrix[tile.y][tile.x].isMine = true;
                for (MatrixTile neighbor : gameMatrix[tile.y][tile.x].getNeighbors(gameMatrix))
                    neighbor.incMinesAround();
            }
            open();
            for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix))
                gameMatrix[tile.y][tile.x].open(); //на первом клике открываются минимум 7 клеток
        }

        public void open() {
            if (!isFirstTileWasClicked())
                openFirstTile();
            if (gameMatrix[y][x].isOpen) return; //клик на уже открытую клетку
            gameMatrix[y][x].isOpen = true;
            if (gameMatrix[y][x].hasFlag) {     //не выполняется при клике мышкой
                flagsLeft++;
                tilesBeforeVictory++;
                gameMatrix[y][x].hasFlag = false;   //если открывается сразу несколько клеток, флаги с открывающихся клеток сбрасываются
            }
            if (gameMatrix[y][x].isMine) { //клик на мину
                endGame(false);
                return;
            }
            tilesBeforeVictory--;
            if (tilesBeforeVictory == 0 && flagsLeft == 0) endGame(true);
            if (gameMatrix[y][x].minesAround == 0)
                for (MatrixTile tile : gameMatrix[y][x].getNeighbors(gameMatrix))
                    gameMatrix[tile.y][tile.x].open();
        }

        public void setFlag() {
            if (gameMatrix[y][x].isOpen) return; //установка флага на открытую клетку
            if (gameMatrix[y][x].hasFlag()) { //удаление флага
                gameMatrix[y][x].hasFlag = false;
                flagsLeft++;
                tilesBeforeVictory++;
                return;
            }
            gameMatrix[y][x].hasFlag = true;
            tilesBeforeVictory--;
            flagsLeft--;
            if (tilesBeforeVictory == 0 && flagsLeft == 0) endGame(true);
        }

        public void incMinesAround() {
            minesAround++;
        }

        List<MatrixTile> getNeighbors(MatrixTile[][] matrix) {
            List<MatrixTile> list = new ArrayList<>();
            if (y == 0) { //если первая строка в матрице
                if (x == 0) { //начало строки
                    list.add(matrix[1][0]);
                    list.add(matrix[0][1]);
                } else if (x == matrix[0].length - 1) { //конец строки
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
                if (y == matrix.length - 1) { //если последняя строка
                    if (x == 0) {       //если первая клетка в строке
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y][x + 1]);
                    } else if (x == matrix[0].length - 1) { //последняя клетка в строке
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
                    } else if (x == matrix[0].length - 1) {
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
                if (y == matrix.length - 1) {
                    if (x == 0) {
                        list.add(matrix[y - 1][x]);
                        list.add(matrix[y - 1][x + 1]);
                        list.add(matrix[y][x + 1]);
                    } else if (x == matrix[0].length - 1) {
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
                    } else if (x == matrix[0].length - 1) {
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj instanceof MatrixTile) {
                MatrixTile other = (MatrixTile) obj;
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