package main.utils;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

/**
 * Special textfield class, which checks the input and allows
 * only integers to be inserted.
 */
public class IntegerTextField extends CustomTextField {

	private final static Logger LOGGER =
			Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	public IntegerTextField(String defaultText) {
		super(defaultText);
	}

	public IntegerTextField() {
		super();
	}

	@Override
	protected boolean isValid(final String value) {
		if (super.isValid(value)) return true;

		try {
			int intVal = Integer.parseInt(value);
			if (intVal == 0 && value.length() > 8) return false;
			else return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public int getInt() {
		try {
			return Integer.parseInt(getText());
		}
		catch (NumberFormatException e) {
			LOGGER.info("Error parsing int (" + getText() +") from field. Error : " + e);
			return 0;
		}
	}
}