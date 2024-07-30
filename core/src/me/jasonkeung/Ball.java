package me.jasonkeung;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Ball {
    public static int INIT_SIZE = 50;
    public static final int SPEED = 1000;
    public static final float BOUNCE_MULTIPLE = 1F;
    public static final float GRAVITY = 0;
    public Circle box;
    public float vX;
    public float vY;
    private LinkedList<Circle> trail;
    private Color color;
    private int rainbowProgress;
    private long lastBounceTick;
    private boolean didBounceThisTick;
    private Texture texture;
    private Sound sound;

    public Ball(int initX, int initY, Vector2 direction) {
        this.box = new Circle(initX, initY, INIT_SIZE);
        this.vX = direction.len() != 0 ? direction.x / direction.len() * SPEED : 0;
        this.vY = direction.len() != 0 ? direction.y / direction.len() * SPEED : 0;
        this.trail = new LinkedList<>(List.of(
                new Circle(), new Circle(), new Circle()));
        this.rainbowProgress = 0;
        this.color = new Color().fromHsv(rainbowProgress, 1, 1);
        this.lastBounceTick = 0;
        this.didBounceThisTick = false;
        this.texture = new Texture("kamala.png");
        this.sound = Gdx.audio.newSound(Gdx.files.internal("c6.mp3"));
    }

    public void update(float deltaTime, Optional<Arena> maybeArena) {
        this.didBounceThisTick = false;
        maybeArena.ifPresent(this::bounceOnArena);
        bounceOnWalls();
        if (didBounceThisTick) {
            this.lastBounceTick = BounceSongGame.tickCount;
        }

        this.vY += (GRAVITY * box.radius * box.radius) * deltaTime;

        updateTrail();
        this.box = new Circle(
                this.box.x + vX * deltaTime,
                this.box.y + vY * deltaTime,
                this.box.radius);
    }

    public void applyBounceEffects() {
//        this.box.radius -= 1;
        if (BounceSongGame.tickCount - lastBounceTick > 3) {
            this.color.fromHsv(rainbowProgress, 1, 1);
            rainbowProgress = (rainbowProgress + 5) % 360;
        }
        this.didBounceThisTick = true;
        this.sound.play();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(this.color);
        shapeRenderer.circle(box.x, box.y, box.radius);
        for (Circle circle : this.trail) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, box.x - box.radius, box.y - box.radius, box.radius * 2, box.radius * 2);
    }

    private void bounceOnArena(Arena arena) {
        float distBetweenCenters =
                Vector2.dst(box.x, box.y, arena.box.x, arena.box.y);
        boolean isPlayerTouchingInnerArenaBounds =
                distBetweenCenters >= arena.box.radius - box.radius &&
                        distBetweenCenters < arena.box.radius;
        if (isPlayerTouchingInnerArenaBounds) {
            applyBounceEffects();
            arena.applyBounceEffects();

            Vector2 normal = new Vector2(
                    arena.box.x - this.box.x,
                    arena.box.y - this.box.y).nor();
            Vector2 incomingV = new Vector2(this.vX, this.vY);
            Vector2 reflectedV = incomingV.sub(normal.scl(2 * incomingV.dot(normal)));
            this.vX = reflectedV.x;
            this.vY = reflectedV.y;

            Vector2 arenaToPlayerDirection = new Vector2(
                    this.box.x - arena.box.x,
                    this.box.y - arena.box.y).nor();
            Vector2 exactContactPoint = arenaToPlayerDirection
                    .scl(arena.box.radius - box.radius)
                    .add(new Vector2(arena.box.x, arena.box.y));
            this.box.x = exactContactPoint.x;
            this.box.y = exactContactPoint.y;

        }
    }

    private void bounceOnWalls() {
        bounceOnBottom();
        bounceOnTop();
        bounceOnLeft();
        bounceOnRight();
    }

    private void bounceOnLeft() {
        if (this.box.x < this.box.radius) {
            applyBounceEffects();
            vX = Math.abs(vX * BOUNCE_MULTIPLE);
            this.box.x = this.box.radius;
        }
    }

    private void bounceOnRight() {
        if (this.box.x > BounceSongGame.WIDTH - this.box.radius) {
            applyBounceEffects();
            vX = -Math.abs(vX * BOUNCE_MULTIPLE);
            this.box.x = BounceSongGame.WIDTH - this.box.radius;
        }
    }

    private void bounceOnTop() {
        if (this.box.y > BounceSongGame.HEIGHT - this.box.radius) {
            applyBounceEffects();
            vY = -Math.abs(vY * BOUNCE_MULTIPLE);
            this.box.y = BounceSongGame.HEIGHT - this.box.radius;
        }
    }

    private void bounceOnBottom() {
        if (this.box.y < this.box.radius) {
            applyBounceEffects();
            vY = Math.abs(BOUNCE_MULTIPLE * vY);
            this.box.y = this.box.radius;
        }
    }

    private void updateTrail() {
        this.trail.removeLast();
        this.trail.addFirst(this.box);
    }

    public void dispose() {
    }


}
