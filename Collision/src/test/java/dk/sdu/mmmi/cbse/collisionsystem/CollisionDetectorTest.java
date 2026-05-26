package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionDetectorTest {

    @Test
    public void collidesWhenDistanceIsLessThanRadiusSum() {
        CollisionDetector detector = new CollisionDetector();
        Entity first = entityAt(100, 100, 10);
        Entity second = entityAt(115, 100, 10);

        assertTrue(detector.collides(first, second));
    }

    @Test
    public void doesNotCollideWhenDistanceIsGreaterThanRadiusSum() {
        CollisionDetector detector = new CollisionDetector();
        Entity first = entityAt(100, 100, 10);
        Entity second = entityAt(130, 100, 10);

        assertFalse(detector.collides(first, second));
    }

    private Entity entityAt(double x, double y, float radius) {
        Entity entity = new Entity();
        entity.setX(x);
        entity.setY(y);
        entity.setRadius(radius);
        return entity;
    }
}
