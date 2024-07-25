package me.jasonkeung;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class BounceSongGame extends ApplicationAdapter {
	public static final int HEIGHT = 1300;
	public static final int WIDTH = HEIGHT * 9 / 16;
	public static final int BACKGROUND_TILE_WIDTH = 1300;
	public static final float UPDATE_DT = 0.005F;

	private Texture backgroundTexture;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private Player player;
	private double accumulatedUpdateTime;
	private Sound note;
	private Arena arena;
	
	@Override
	public void create() {
//		backgroundTexture = new Texture("white.png");
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		player = new Player(500, 500, Player.Face.DIRT, new Vector2(0, 1));
		accumulatedUpdateTime = 0;
		note = Gdx.audio.newSound(Gdx.files.internal("c6.mp3"));
		arena = new Arena();
	}

	private void update(float deltaTime) {
		player.update(deltaTime, arena);
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


//		batch.begin();
//		batch.draw(
//				backgroundTexture,
//				0,
//				0,
//				BACKGROUND_TILE_WIDTH,
//				BACKGROUND_TILE_WIDTH);
//		batch.end();
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
