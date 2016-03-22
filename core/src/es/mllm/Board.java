package es.mllm;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by luism on 21/03/2016.
 */
public class Board {

    private Chip[][] chips;
    private Stack<Chip> red_chips, blue_chips;

    private Table blue_chips_table, red_chips_table;

    private int turn = 0;
    private boolean gameOver = false;

    public Board(Chip[][] chips, Stack<Chip> red_chips, Stack<Chip> blue_chips, int turn, boolean gameOver) {
        this.chips = new Chip[chips.length][chips[0].length];
        for (int i = 0; i < chips.length; i++) {
            for (int j = 0; j <= i; j++) {
                Chip chip = chips[i][j];
                this.chips[i][j] = chip.copy();
            }
        }

        this.blue_chips = new Stack<Chip>();

        Object[] array = blue_chips.toArray();
        for (Object o : array) {
            Chip chip = (Chip) o;
            this.blue_chips.push(chip.copy());
        }

        this.red_chips = new Stack<Chip>();

        array = red_chips.toArray();
        for (Object o : array) {
            Chip chip = (Chip) o;
            this.red_chips.push(chip.copy());
        }

        this.turn = turn;
        this.gameOver = gameOver;
    }

    public Board() {

    }

    public void addBoard(Table table) {
        chips = new Chip[6][6];
        Table board = new Table();
        for (int i = 0; i < 6; i++) {
            board.row();
            Table row = new Table();
            for (int j = 0; j <= i; j++) {
                Chip chip = new Chip(i, j);
                chips[i][j] = chip;
                row.add(chip).size(32).pad(0, 1, 0, 1);
            }
            board.add(row);
        }
        table.add(board).expand();
    }

    public void addBlueChips(Table table) {
        blue_chips = new Stack<Chip>();


        blue_chips_table = new Table();
        set_blue_chips_table();

        table.add(blue_chips_table).padTop(32).height(64);
    }

    public void addRedChips(Table table) {
        red_chips = new Stack<Chip>();

        red_chips_table = new Table();
        set_red_chips_table();

        table.add(red_chips_table).padBottom(32).height(64);
    }

    public void set_red_chips_table() {
        red_chips_table.clear();
        for (int i = 10; i > 0; i--) {
            if (i == 5) {
                red_chips_table.row();
            }
            Chip chip = new Chip(-1, -1, 1, i);
            red_chips.push(chip);
            red_chips_table.add(chip);
        }
    }

    public void set_blue_chips_table() {
        blue_chips_table.clear();
        for (int i = 10; i > 0; i--) {
            if (i == 5) {
                blue_chips_table.row();
            }
            Chip chip = new Chip(-1, -1, 0, i);
            blue_chips.push(chip);
            blue_chips_table.add(chip);
        }
    }

    public List<Board> getNextStates() {
        List<Board> nextStates = new ArrayList<Board>();


        for (Chip[] row : chips) {
            for (Chip chip : row) {
                if (chip != null && chip.isEmpty()) {
                    Board clone = copy();
                    Chip nextChip = clone.getNextChip();
                    clone.setMove(chip, nextChip);
                    nextStates.add(clone);
                }
            }
        }

        return nextStates;
    }

    public void setMove(Chip target, Chip chip) {
        chips[target.getI()][target.getJ()].setMove(chip);
        chip.remove();
    }

    public void play() {
        List<Board> nextStates = getNextStates();
        Board nextState = nextStates.get(MathUtils.random(nextStates.size() - 1));

        for (int i = 0; i < chips.length; i++) {
            for (int j = 0; j <= i; j++) {
                Chip chip = nextState.chips[i][j];
                chips[i][j].setMove(chip);
            }
        }

        getNextChip();
        gameOver = nextState.gameOver;

    }

    public Chip getNextChip() {
        Chip chip;
        if (turn == 0) {
            chip = blue_chips.pop();
        } else {
            chip = red_chips.pop();
        }
        chip.remove();
        turn = (1 + turn) % 2;

        return chip;
    }

    public void addToList(List<Chip> lastChips, int x, int y) {
        try {
            Chip e = chips[x][y];
            if (e != null) {
                lastChips.add(e);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean checkForEndGame() {

        if (blue_chips.size() != 0 || red_chips.size() != 0) {
            return false;
        }

        gameOver = true;

        Chip blackHole = null;

        for (Chip[] row : chips) {
            for (Chip chip : row) {
                if (chip != null && chip.isEmpty()) {
                    blackHole = chip;
                }
            }
        }

        if (blackHole == null) {
            System.err.println("This should never happen");
            return false;
        }

        int x = blackHole.getI();
        int y = blackHole.getJ();


        ArrayList<Chip> lastChips = new ArrayList<Chip>();


        addToList(lastChips, x, y - 1);
        addToList(lastChips, x, y + 1);
        addToList(lastChips, x - 1, y - 1);
        addToList(lastChips, x - 1, y);
        addToList(lastChips, x + 1, y);
        addToList(lastChips, x + 1, y + 1);

        printBoard();

        for (Chip[] row : chips) {
            for (Chip chip : row) {
                if (chip != null && !lastChips.contains(chip)) {
                    chip.reset();
                }
            }
        }

        int red_points = 0;
        int blue_points = 0;

        for (Chip lastChip : lastChips) {
            if (lastChip.getPlayer() == 0) {
                blue_points += lastChip.getNumber();
            } else if (lastChip.getPlayer() == 1) {
                red_points += lastChip.getNumber();
            }
        }
        System.out.println("Red:  " + red_points);
        System.out.println("Blue: " + blue_points);
        System.out.println(lastChips);
        return true;
    }

    public void printBoard() {
        for (int j = 0; j < chips[0].length; j++) {
            for (int i = 0; i < chips.length; i++) {
                Chip chip = chips[j][i];
                if (chip != null) {
                    System.out.printf("%20s", chip);
                }
            }
            System.out.println();
        }
    }

    public int getTurn() {
        return turn;
    }

    public Board copy() {
        return new Board(chips, red_chips, blue_chips, turn, gameOver);
    }

    public void reset() {
        gameOver = false;
        for (int i = 0; i < chips.length; i++) {
            for (int j = 0; j <= i; j++) {
                chips[i][j].reset();
            }
        }

        set_red_chips_table();
        set_blue_chips_table();

        turn = (turn + 1) % 2;
        if (turn == 1) {
            play();
        }
    }
}
