package es.mllm;

import com.badlogic.gdx.Gdx;
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

    public GameStage() {
        super(new FitViewport(204, 204 * 16 / 9));

        board = new Board();
        Chip.loadChips(board);

        Table table = new Table();
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
        board.printBoard();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (board.getTurn() == 1 && !board.isGameOver()) {
                    board.play();
                    board.checkForEndGame();
                } else if (board.isGameOver()) {
                    board.reset();
                }
            }
        });

    }


}
