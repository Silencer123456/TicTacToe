package main.engine;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;


public abstract class Sprite {

	public List animations = new ArrayList<>();
	public Node node;

	public double vX = 0;
	public double vY = 0;

	public boolean isDead = false;

	public abstract void update();

	protected boolean collide(Sprite other) {
		return false;
	}
}
