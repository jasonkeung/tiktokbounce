package me.jasonkeung;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class BounceSongGame extends ApplicationAdapter {
	public static final int HEIGHT = 1300;
	public static final int WIDTH = HEIGHT * 9 / 16;
	public static final float UPDATE_DT = 0.005F;

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private Player player;
	private double accumulatedUpdateTime;
	private Arena arena;
	
	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		player = new Player(500, 500, new Vector2(0, 1));
		accumulatedUpdateTime = 0;
		arena = new Arena();
	}

	private void update(float deltaTime) {
		player.update(deltaTime, arena);
		arena.update(deltaTime);
	}


	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		float deltaTime = Gdx.graphics.getDeltaTime();

		accumulatedUpdateTime += deltaTime;
		while (accumulatedUpdateTime >= UPDATE_DT) {
			update(UPDATE_DT);
			accumulatedUpdateTime -= UPDATE_DT;
		}

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		arena.draw(shapeRenderer);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		player.draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		arena.dispose();
		player.dispose();
	}
}
