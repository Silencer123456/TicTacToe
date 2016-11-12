package main.game;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Tile extends StackPane {

    Board board;

    private Image noughtImage;
    private Image crossImage;

    Point2D position = new Point2D.Double();

    private final ImageView imageView = new ImageView();

    private ReadOnlyObjectWrapper<TilesState> tileState = new ReadOnlyObjectWrapper<>(TilesState.EMPTY);
    public ReadOnlyObjectProperty<TilesState> stateProperty() {
        return tileState.getReadOnlyProperty();
    }

    public TilesState getState() {
        return tileState.get();
    }

    public Tile(Board board, int tileWidth, int x, int y) {
        this.board = board;
        this.position.setLocation(x, y);
        setId("tile");

        setMaxWidth(tileWidth);
        setMaxHeight(tileWidth);

         try {
           InputStream nought = Files.newInputStream(Paths.get("resources/images/nought.png"));
            InputStream cross = Files.newInputStream(Paths.get("resources/images/cross.png"));

            noughtImage = new Image(nought);
            crossImage = new Image(cross);

        } catch (IOException e) {
            e.printStackTrace();
        }
        setSkin();
    }

    private void setSkin() {
        imageView.setMouseTransparent(true);
        getChildren().add(imageView);

        setOnMousePressed(event -> board.pressed((int) position.getX(), (int) position.getY()));

        stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
                case EMPTY: imageView.setImage(null);
                    break;
                case NOUGHT: imageView.setImage(noughtImage);
                    break;
                case CROSS: imageView.setImage(crossImage);
                    break;
            }
        });
    }
}
