package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Text hud = new Text(10, 20, "Score: 0 | Asteroids: 0");
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServices;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;

    Game(List<IGamePluginService> gamePluginServices,
         List<IEntityProcessingService> entityProcessingServices,
         List<IPostEntityProcessingService> postEntityProcessingServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;
    }

    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(hud);

        Scene scene = new Scene(gameWindow);
        installKeyboardHandlers(scene);

        for (IGamePluginService gamePlugin : gamePluginServices) {
            gamePlugin.start(gameData, world);
        }

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void installKeyboardHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> setKey(event.getCode(), true));
        scene.setOnKeyReleased(event -> setKey(event.getCode(), false));
    }

    private void setKey(KeyCode keyCode, boolean pressed) {
        if (keyCode == KeyCode.LEFT) {
            gameData.getKeys().setKey(GameKeys.left, pressed);
        } else if (keyCode == KeyCode.RIGHT) {
            gameData.getKeys().setKey(GameKeys.right, pressed);
        } else if (keyCode == KeyCode.UP) {
            gameData.getKeys().setKey(GameKeys.up, pressed);
        } else if (keyCode == KeyCode.SPACE) {
            gameData.getKeys().setKey(GameKeys.space, pressed);
        }
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : entityProcessingServices) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessingServices) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity entity : polygons.keySet()) {
            if (world.getEntity(entity.getID()) == null) {
                Polygon removedPolygon = polygons.remove(entity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                applyEntityColor(entity, polygon);
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }

        hud.setText("Score: " + gameData.getScore() + " | Asteroids destroyed: " + gameData.getDestroyedAsteroids());
        hud.toFront();
    }

    private void applyEntityColor(Entity entity, Polygon polygon) {
        String typeName = entity.getClass().getSimpleName();
        if ("Player".equals(typeName)) {
            polygon.setFill(Color.BLUE);
        } else if ("Enemy".equals(typeName)) {
            polygon.setFill(Color.RED);
        } else if ("Asteroid".equals(typeName)) {
            polygon.setFill(Color.GREY);
        }
    }
}
