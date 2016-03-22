package es.mllm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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
        super(player == 0 ? chips_blue[n - 1] : player == 1 ? chips_red[n - 1] : chip_blank);
        this.player = player;
        this.number = n;
        this.i = i;
        this.j = j;
        setSize(32, 32);
    }

    public Chip(int i, int j) {
        this(i, j, -1, 0);


        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player == -1 && board.canPlayHuman()) {
                    playMove(board.getNextChip());
                }
            }
        });

    }

    public void setNumber(int number) {
        if (player == 0) {
            getStyle().imageUp = chips_blue[number - 1];
        } else if (player == 1){
            getStyle().imageUp = chips_red[number - 1];
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

    public void playMove(final Chip chip) {
        if (!Board.stage.isActionFinished()) {
            return;
        }
        SequenceAction action = new SequenceAction(
                getMoveToChipAction(),
                getMoveChipAction(chip)
        );

        Board.stage.setAction(action);
        Board.stage.setToFront(chip);
        chip.addAction(action);
    }

    public MoveToAction getMoveToChipAction() {
        MoveToAction moveToAction = new MoveToAction();

        Vector2 pos = new Vector2(getX(), getY());
        Vector2 dest = getParent().localToStageCoordinates(pos);

        moveToAction.setPosition(dest.x, dest.y);
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sine);
        return moveToAction;
    }

    public RunnableAction getMoveChipAction(final Chip chip) {
        RunnableAction moveAction = new RunnableAction();
        moveAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                setMove(chip);
                chip.remove();
                Board.stage.setAction(null);
                board.checkForEndGame();
            }
        });
        return moveAction;
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

        Texture chip_blank_texture = new Texture("chip_blank.png");
        Texture chips_red_texture = new Texture("chips_red.png");
        Texture chips_blue_texture = new Texture("chips_blue.png");

        chip_blank_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        chips_red_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        chips_blue_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        chip_blank = new TextureRegionDrawable(new TextureRegion(chip_blank_texture));

        TextureRegion[] textureRegions_red = TextureRegion.split(chips_red_texture, 64, 64)[0];

        chips_red = new TextureRegionDrawable[textureRegions_red.length];
        for (int i = 0; i < textureRegions_red.length; i++) {
            chips_red[i] = new TextureRegionDrawable(textureRegions_red[i]);
        }
        TextureRegion[] textureRegions_blue = TextureRegion.split(chips_blue_texture, 64, 64)[0];
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

    public boolean equals(Chip chip) {
        return chip.number == number && chip.player == player;
    }
}
