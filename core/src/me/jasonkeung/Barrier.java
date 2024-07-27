package me.jasonkeung;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Barrier {

    public enum FaceDirection {
        XPOS, XNEG, YPOS, YNEG
    }
    public static final int LENGTH = 50;
    public static final int DEPTH = LENGTH / 5;
    private Texture img;
    private Rectangle box;
    private FaceDirection faceDirection;
    private boolean isGone;

    public Barrier(float initX, float initY, FaceDirection faceDirection) {
        this.img = new Texture("navy.png");
        this.faceDirection = faceDirection;
        if (faceDirection.equals(FaceDirection.XPOS) || faceDirection.equals(FaceDirection.XNEG)) {
            this.box = new Rectangle(initX, initY, DEPTH, LENGTH);
        } else {
            this.box = new Rectangle(initX, initY, LENGTH, DEPTH);
        }
    }

    public void dispose() {
        img.dispose();
    }

    public void draw(SpriteBatch batch) {
        if (!isGone) {
            batch.draw(img, box.x, box.y, box.width, box.height);
        }
    }


    public static void saveBarriers(String midiFileName, List<Barrier> barriers) {
        FileHandle fileHandle = Gdx.files.local("barriers/" + midiFileName);
        fileHandle.writeString("", false);
        for (Barrier b : barriers) {
            fileHandle.writeString(b.box.x + " " + b.box.y + " " + b.faceDirection + "\n", true);
        }
    }

    public void bounce(Ball ball) {
        Vector2 newV;
        newV = getNormalBounceVector(ball.vX, ball.vY);
        ball.vX = (int) newV.x;
        ball.vY = (int) newV.y;

//        if (faceDirection.equals(FaceDirection.XPOS)) {
//            ball.getBox().setX(this.getBox().x + this.getBox().width);
//        } else if (faceDirection.equals(FaceDirection.XNEG)) {
//            ball.getBox().setX(this.getBox().x - ball.getBox().width);
//        } else if (faceDirection.equals(FaceDirection.YPOS)) {
//            ball.getBox().setY(this.getBox().y + this.getBox().height);
//        } else {
//            ball.getBox().setY(this.getBox().y - ball.getBox().height);
//        }
    }

    public Vector2 getNormalBounceVector(float vX, float vY) {
        if (faceDirection.equals(FaceDirection.XPOS)) {
            return new Vector2(Math.abs(vX), vY);
        } else if (faceDirection.equals(FaceDirection.XNEG)) {
            return new Vector2(-Math.abs(vX), vY);
        } else if (faceDirection.equals(FaceDirection.YPOS)) {
            return new Vector2(vX, Math.abs(vY));
        } else {
            return new Vector2(vX, -Math.abs(vY));
        }
    }

    public Vector2 getRandomBounceVector(Vector2 incomingVector) {
        int randOtherDirectionIndex = (int) (Math.random() * 3);
        float incomingSpeed = incomingVector.len();
        if (faceDirection.equals(FaceDirection.XPOS)) {
            if (randOtherDirectionIndex == 0) {
                return new Vector2(incomingSpeed, 0);
            } else if (randOtherDirectionIndex == 1) {
                return new Vector2(0, incomingSpeed);
            } else {
                return new Vector2(0, -incomingSpeed);
            }
        } else if (faceDirection.equals(FaceDirection.XNEG)) {
            if (randOtherDirectionIndex == 0) {
                return new Vector2(-incomingSpeed, 0);
            } else if (randOtherDirectionIndex == 1) {
                return new Vector2(0, incomingSpeed);
            } else {
                return new Vector2(0, -incomingSpeed);
            }
        } else if (faceDirection.equals(FaceDirection.YPOS)) {
            if (randOtherDirectionIndex == 0) {
                return new Vector2(incomingSpeed, 0);
            } else if (randOtherDirectionIndex == 1) {
                return new Vector2(-incomingSpeed, 0);
            } else {
                return new Vector2(0, incomingSpeed);
            }
        } else {
            if (randOtherDirectionIndex == 0) {
                return new Vector2(incomingSpeed, 0);
            } else if (randOtherDirectionIndex == 1) {
                return new Vector2(-incomingSpeed, 0);
            } else {
                return new Vector2(0, -incomingSpeed);
            }
        }
    }

    public Rectangle getBox() {
        return box;
    }

    public void setBox(Rectangle box) {
        this.box = box;
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public FaceDirection getDirection() {
        return faceDirection;
    }

    public void setDirection(FaceDirection faceDirection) {
        this.faceDirection = faceDirection;
    }

    public boolean getIsGone() {
        return this.isGone;
    }
}
