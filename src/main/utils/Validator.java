package main.utils;


import java.util.regex.Pattern;

public class Validator {

	/**
	 * Maximal string length allowed
	 * The value of this constant is {@value}.
	 */
	public static final int MAX_STRING_LENGTH = 20;

	private static final Pattern IP_PATTERN = Pattern.compile(
			"^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validateConnectInputs(final String ip) {
		boolean result = false;

		if ((IP_PATTERN.matcher(ip).matches() || ip.equals("localhost"))) result = true;
		return  result;
	}

	/**
	 * Validates string.
	 * Conditions: String is less than {@value Validator#MAX_STRING_LENGTH}</code>
	 * @param string - <code>String</code> to validate
	 * @return true if <code>String</code> meets conditions else false
	 */
	public static boolean validateString(String string) {
		boolean valid = false;
		if (string.length() <= MAX_STRING_LENGTH) {
			valid = true;
		}

		return valid;
	}
}
