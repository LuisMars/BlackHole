package es.mllm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 * Created by luism on 20/03/2016.
 */
public class GameStage extends Stage {

    private Board board;
    private Action action;
    private Table table;
    public GameStage() {
        super(new FitViewport(210, 336));

        board = new Board(this);
        Chip.loadChips(board);


        table = new Table();
        addActor(table);

        table.setFillParent(true);
        table.center();



        board.addRedChips(table);
        table.row();

        board.addBoard(table);
        table.row();

        board.addBlueChips(table);


        Gdx.input.setInputProcessor(this);

        //setDebugAll(true);
        //board.printBoard();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (board.isGameOver()) {
                    board.reset();
                }
            }
        });

    }


    public boolean isActionFinished() {
        return action == null;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setToFront(Actor actor) {
        Vector2 pos = new Vector2(actor.getX(), actor.getY());
        actor.getParent().localToStageCoordinates(pos);
        table.addActorAt(9999, actor);
        actor.setPosition(pos.x, pos.y);
    }
}
