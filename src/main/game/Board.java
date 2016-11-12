package main.game;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.MainController;

public class Board extends GridPane {

    private MainController mainController;

    private Tile[][] tiles;

    public Board(MainController mainController, int boardSize, int width) {
        this.mainController = mainController;

        setId("gameBoard");
        setHgap(1);
        setVgap(1);

        //setPadding(new Insets(20, 20, 20, 20));

        int spacingSize = (int)((boardSize-1)*getHgap());
        int paddingSize = (int)(getPadding().getLeft() + getPadding().getRight());
        int tileWidth = width-paddingSize-spacingSize / boardSize;

        tiles = new Tile[boardSize][boardSize];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {

                tiles[i][j] = new Tile(this, tileWidth, i, j);

                setHgrow(tiles[i][j], Priority.ALWAYS);
                setVgrow(tiles[i][j], Priority.ALWAYS);

                add(tiles[i][j], i, j);
            }
        }
    }

    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    public void pressed(int x, int y) {
        mainController.tilePressed(x, y);
    }
}
