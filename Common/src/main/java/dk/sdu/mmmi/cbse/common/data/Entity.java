package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();

    private double[] polygonCoordinates = new double[0];
    private double x;
    private double y;
    private double rotation;
    private double velocityX;
    private double velocityY;
    private float radius;
    private int life = 1;

    public String getID() {
        return id.toString();
    }

    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates == null ? new double[0] : coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setRotation(double rotation) {
        this.rotation = normalizeDegrees(rotation);
    }

    public double getRotation() {
        return rotation;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = Math.max(1, life);
    }

    public void damage(int amount) {
        life -= Math.max(1, amount);
    }

    public boolean isDead() {
        return life <= 0;
    }

    private double normalizeDegrees(double value) {
        double normalized = value % 360;
        return normalized < 0 ? normalized + 360 : normalized;
    }
}
