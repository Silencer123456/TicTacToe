package main.game;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ErrorText extends StackPane {
	private Text text;

	public ErrorText(String name) {
		text = new Text(name);
		text.getFont();
		text.setFill(Color.RED);
		text.setFont(Font.font(null, FontWeight.BOLD, 14));

		setAlignment(Pos.CENTER_LEFT);

		getChildren().add(text);
	}

	public void setText(String newText) {
		text.setText(newText);
	}

	public void clear() {
		text.setText("");
	}
}
