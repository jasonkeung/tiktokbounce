package me.jasonkeung;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.LinkedList;
import java.util.List;

public class Player {
    public enum Face {
        DIRT, STEVE;

        public static Texture getTexture(Face face) {
            if (face.equals(DIRT)) {
                return new Texture("dirt.jpg");
            } else if (face.equals(STEVE)) {
                return new Texture("steve.jpg");
            }
            return null;
        }
    }
    public static int INIT_SIZE = 25;
    public static final int SPEED = 1000;
    public static final float BOUNCE_MULTIPLE = .9F;
    public static final float GRAVITY = -5;
    private Texture img;
    private Circle box;
    private float vX;
    private float vY;
    private LinkedList<Circle> trail;
    private Color color;
    private int rainbowProgress;

    public Player(int initX, int initY, Face face, Vector2 direction) {
        this.img = Face.getTexture(face);
        this.box = new Circle(initX + INIT_SIZE, initY + INIT_SIZE, INIT_SIZE);
        this.vX = direction.x / direction.len() * SPEED;
        this.vY = direction.y / direction.len() * SPEED;
        this.trail = new LinkedList<>(List.of(
                new Circle(), new Circle(), new Circle()));
        this.rainbowProgress = 0;
        this.color = new Color().fromHsv(rainbowProgress, 1, 1);
    }

    public void update(float deltaTime, Arena arena) {
        bounceOnArena(arena);
        bounceOnWalls();
        this.vY += (GRAVITY * box.radius * box.radius) * deltaTime;

        updateTrail();
        this.box = new Circle(
                this.box.x + vX * deltaTime,
                this.box.y + vY * deltaTime,
                this.box.radius);
    }

    private void bounceOnArena(Arena arena) {
        float distBetweenCenters =
                Vector2.dst(box.x, box.y, arena.getBox().x, arena.getBox().y);
        boolean isPlayerTouchingInnerArenaBounds =
                distBetweenCenters >= arena.getBox().radius - box.radius &&
                        distBetweenCenters < arena.getBox().radius;
        if (isPlayerTouchingInnerArenaBounds) {
            applyBounceEffects();
            arena.applyBounceEffects();
            // create normal vector from two centers
            Vector2 normal = new Vector2(
                    arena.getBox().x - this.box.x,
                    arena.getBox().y - this.box.y).nor();
            Vector2 incomingV = new Vector2(this.vX, this.vY);
            Vector2 reflectedV = incomingV.sub(normal.scl(2 * incomingV.dot(normal)));
            this.vX = reflectedV.x;
            this.vY = reflectedV.y;
            Vector2 exactContactPoint = normal.nor()
                    .scl(arena.getBox().radius - box.radius)
                    .add(new Vector2(arena.getBox().x, arena.getBox().y));
            this.box.x = exactContactPoint.x;
            this.box.y = exactContactPoint.y;

        }
    }

    private void bounceOnWalls() {
        if (this.box.x < 0 + this.box.radius) {
            applyBounceEffects();
            vX = Math.abs(vX * BOUNCE_MULTIPLE);
            this.box.x = this.box.radius;
        } else if (this.box.x > BounceSongGame.WIDTH - this.box.radius) {
            applyBounceEffects();
            vX = -Math.abs(vX * BOUNCE_MULTIPLE);
            this.box.x = BounceSongGame.WIDTH - this.box.radius;
        } else if (this.box.y < 0 + this.box.radius) {
            applyBounceEffects();
            vY = Math.abs(BOUNCE_MULTIPLE * vY);
            this.box.y = this.box.radius;
        } else if (this.box.y > BounceSongGame.HEIGHT - this.box.radius) {
            applyBounceEffects();
            vY = -Math.abs(BOUNCE_MULTIPLE * vY);
            this.box.y = BounceSongGame.HEIGHT - this.box.radius;
        }
    }

    private void applyBounceEffects() {
//        this.box.radius -= 1;
        this.color.fromHsv(rainbowProgress % 360, 1, 1);
        rainbowProgress += 5;
    }

    private void updateTrail() {
        this.trail.removeLast();
        this.trail.addFirst(this.box);
    }

    public void dispose() {
        img.dispose();
    }

    public void draw(SpriteBatch batch) {
//        batch.draw(img, box.x, box.y, box.width, box.height);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        Color currentColor = new Color(shapeRenderer.getColor());
        shapeRenderer.setColor(this.color);
        shapeRenderer.circle(box.x, box.y, box.radius);
        for (Circle circle : this.trail) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }
        shapeRenderer.setColor(currentColor);
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public float getVX() {
        return vX;
    }

    public void setVX(float vX) {
        this.vX = vX;
    }

    public float getVY() {
        return vY;
    }

    public void setVY(float vY) {
        this.vY = vY;
    }

}
