package es.mllm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by luism on 20/03/2016.
 */
public class Chip extends ImageButton {

    private static TextureRegionDrawable[] chips_red, chips_blue;
    private static TextureRegionDrawable chip_blank;
    private static Board board;
    private final int i;
    private final int j;
    private int player;
    private int number = 0;

    public Chip(int i, int j, int player, int n) {
        super(player == 0 ? chips_blue[n] : player == 1 ? chips_red[n] : chip_blank);
        this.player = player;
        this.number = n;
        this.i = i;
        this.j = j;

    }

    public Chip(int i, int j) {
        this(i, j, -1, 0);


        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player == -1 && !board.isGameOver() && board.getTurn() == 0) {

                    Chip chip = board.getNextChip();
                    setMove(chip);
                    board.checkForEndGame();

                }
            }
        });

    }



    public void setNumber(int number) {
        if (player == 0) {
            getStyle().imageUp = chips_blue[number];
        } else if (player == 1){
            getStyle().imageUp = chips_red[number];
        } else {
            getStyle().imageUp = chip_blank;
        }
        this.number = number;

    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void reset() {
        setMove(-1, 0);
    }

    public void setMove(Chip chip) {
        setMove(chip.getPlayer(), chip.getNumber());
    }

    public void setMove(int player, int number) {
        setPlayer(player);
        setNumber(number);
    }

    public static void loadChips(Board board) {
        Chip.board = board;

        chip_blank = new TextureRegionDrawable(new TextureRegion(new Texture("chip_blank.png")));

        TextureRegion[] textureRegions_red = TextureRegion.split(new Texture("chips_red.png"), 32, 32)[0];
        chips_red = new TextureRegionDrawable[textureRegions_red.length];
        for (int i = 0; i < textureRegions_red.length; i++) {
            chips_red[i] = new TextureRegionDrawable(textureRegions_red[i]);
        }
        TextureRegion[] textureRegions_blue = TextureRegion.split(new Texture("chips_blue.png"), 32, 32)[0];
        chips_blue = new TextureRegionDrawable[textureRegions_blue.length];
        for (int i = 0; i < textureRegions_blue.length; i++) {
            chips_blue[i] = new TextureRegionDrawable(textureRegions_blue[i]);
        }
    }

    @Override
    public String toString() {
        String s =  "[{" + i + ", " + j + "}";
        if (player != -1) {
            s += ", {" + number;
            if (player == 0) {
                s += ", B" + "}]";
            } else {
                s += ", R" + "}]";
            }
        }
        else {
            s += "" + "]";
        }
        return s;
    }

    public int getPlayer() {
        return player;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getNumber() {
        return number;
    }

    public boolean isEmpty() {
        return getPlayer() == -1;
    }

    public Chip copy() {
        return new Chip(i, j, player, number);
    }
}
