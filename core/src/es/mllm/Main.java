package es.mllm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {

	@Override
	public void create() {
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
		setScreen(new GameScreen());
	}
}
