package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Bullet extends Entity {

    private final long createAtMilli = System.currentTimeMillis();
    private String ownerId;
    private String ownerType;
    private double distanceTravelled;
    private double maxDistance = 650;
    private int damage = 1;

    public long getCreateAtMilli() {
        return createAtMilli;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void addDistanceTravelled(double distance) {
        this.distanceTravelled += Math.max(0, distance);
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(1, damage);
    }
}
