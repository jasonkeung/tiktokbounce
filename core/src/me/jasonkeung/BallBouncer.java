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

                Vector2 normal = new Vector2(ball1.box.x - ball2.box.x, ball1.box.y - ball2.box.y).nor();
                Vector2 relativeVelocity = new Vector2(ball1.vX - ball2.vX, ball1.vY - ball2.vY);
                float normalDotRelativeVelocity = relativeVelocity.dot(normal);

                Vector2 impulse = normal.scl(2 * normalDotRelativeVelocity / 2);

                ball1.vX -= impulse.x;
                ball1.vY -= impulse.y;

                ball2.vX += impulse.x;
                ball2.vY += impulse.y;

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
