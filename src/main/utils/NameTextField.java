package main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameTextField extends CustomTextField {

	private Pattern p = Pattern.compile("[^a-z0-9.]", Pattern.CASE_INSENSITIVE);

	public NameTextField() {
		super();
	}

	public NameTextField(String defaultValue) {
		super(defaultValue);
	}

	@Override
	protected boolean isValid(final String value) {
		if (super.isValid(value)) return true;

		Matcher m = p.matcher(value);
		boolean containsSpecial = m.find();

		if (containsSpecial) return false;

		if (value.length() <= Validator.MAX_STRING_LENGTH) return true;

		return false;
	}
}
