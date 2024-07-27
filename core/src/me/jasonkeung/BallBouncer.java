package me.jasonkeung;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BallBouncer {


    public BallBouncer() {
    }

    public void update(List<Ball> balls) {
        for (List<Ball> pair : getBallPairs(balls)) {
            Ball ball1 = pair.get(0);
            Ball ball2 = pair.get(1);

            boolean areBallsTouching =
                    Vector2.dst(ball1.box.x, ball1.box.y, ball2.box.x, ball2.box.y) <= ball1.box.radius + ball2.box.radius;
            if (areBallsTouching) {
                ball1.applyBounceEffects();
                ball2.applyBounceEffects();

                long initialKin = getKineticEnergy(ball1, ball2);

                Vector2 normal1 = new Vector2(
                        ball1.box.x - ball2.box.x,
                        ball1.box.y - ball2.box.y).nor();
                Vector2 shiftedV1 = new Vector2(
                        ball1.vX - ball2.vX,
                        ball1.vY - ball2.vY);
                Vector2 newV1 = new Vector2(ball1.vX, ball1.vY);
                newV1.sub(normal1.scl(shiftedV1.dot(normal1)));
                ball1.vX = newV1.x;
                ball1.vY = newV1.y;


                Vector2 normal2 = new Vector2(
                        ball1.box.x - ball2.box.x,
                        ball1.box.y - ball2.box.y).nor();
                Vector2 shiftedV2 = new Vector2(
                        ball2.vX - ball1.vX,
                        ball2.vY - ball1.vY);
                Vector2 newV2 = new Vector2(ball2.vX, ball2.vY);
                newV2.sub(normal2.scl(shiftedV2.dot(normal2)));
                ball2.vX = newV2.x;
                ball2.vY = newV2.y;

                long endKin = getKineticEnergy(ball1, ball2);

                assert initialKin == endKin;

                Vector2 ball2ToBall1Direction = new Vector2(
                        ball1.box.x - ball2.box.x,
                        ball1.box.y - ball2.box.y).nor();
                Vector2 exactContactPoint = ball2ToBall1Direction
                        .scl(ball2.box.radius + ball1.box.radius)
                        .add(new Vector2(ball2.box.x, ball2.box.y));
                ball1.box.x = exactContactPoint.x;
                ball1.box.y = exactContactPoint.y;
            }
        }
    }

    private long getKineticEnergy(Ball ball1, Ball ball2) {
        Vector2 b1V = new Vector2(ball1.vX, ball1.vY);
        Vector2 b2V = new Vector2(ball2.vX, ball2.vY);
        return Math.round(.5 * b1V.len2() + .5 * b2V.len2());
    }


    private List<List<Ball>> getBallPairs(List<Ball> balls) {
        List<List<Ball>> pairs = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                pairs.add(new ArrayList<>(List.of(balls.get(i), balls.get(j))));
            }
        }

        return pairs;
    }

}
