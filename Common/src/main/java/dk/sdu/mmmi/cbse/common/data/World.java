package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        if (entity != null) {
            entityMap.remove(entity.getID());
        }
    }

    public Collection<Entity> getEntities() {
        return new ArrayList<>(entityMap.values());
    }

    @SafeVarargs
    public final <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> matches = new ArrayList<>();
        for (Entity entity : getEntities()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(entity.getClass())) {
                    matches.add(entity);
                }
            }
        }
        return matches;
    }

    public Entity getEntity(String id) {
        return entityMap.get(id);
    }
}
