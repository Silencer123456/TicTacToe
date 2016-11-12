package main.utils;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public abstract class CustomTextField extends TextField {

	public CustomTextField() {
		this("");
	}

	public CustomTextField(String defaultText) {
		super(defaultText);

		addEventFilter(KeyEvent.KEY_TYPED, event -> {
			if (!isValid(getText())) {
				event.consume();
			}
		});

		textProperty().addListener((observableValue, oldValue, newValue) -> {
			if(!isValid(newValue)) {
				setText(oldValue);
			}
		});
	}

	protected boolean isValid(final String value) {
		if (value.length() == 0 || value.equals("-")) {
			return true;
		}
		return false;
	}

	public boolean isEmpty() {
		return this.getText().isEmpty();
	}
}
