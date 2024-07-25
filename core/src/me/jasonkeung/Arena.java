package me.jasonkeung;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import static me.jasonkeung.BounceSongGame.HEIGHT;
import static me.jasonkeung.BounceSongGame.WIDTH;

public class Arena {

    private Circle box;

    public Arena() {
        this.box = new Circle((float) (WIDTH / 2), (float) (HEIGHT / 2), (float) (WIDTH / 2F * .95));

    }

    public void update(float deltaTime) {

    }

    public void applyBounceEffects() {
//        box.radius -= 1;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        Color savedCurrentColor = new Color(shapeRenderer.getColor());
        Gdx.gl.glLineWidth(5);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(box.x, box.y, box.radius);
        shapeRenderer.setColor(savedCurrentColor);
    }

    public void dispose() {

    }

    public Circle getBox() {
        return this.box;
    }

}
