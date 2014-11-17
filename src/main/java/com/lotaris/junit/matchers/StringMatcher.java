package com.lotaris.junit.matchers;

import org.hamcrest.CustomMatcher;

/**
 * A custom string matcher. It can be used to matches that an API key we receive match few rules. (Length pattern etc)
 *
 * @author Laurent Prevost <laurent.prevost@forbes-digital.com>
 */
public abstract class StringMatcher extends CustomMatcher<String> {

	private StringMatcher(String description) {
		super(description);
	}

	@Override
	public abstract boolean matches(Object item);

	/**
	 * Matcher to check the length of a string
	 *
	 * @param length The expected length of the string
	 * @return The corresponding String Matcher
	 */
	public static StringMatcher length(final int length) {
		return new StringMatcher("length should match " + length) {

			@Override
			public boolean matches(Object item) {
				return ((String)item).length() == length;
			}
		};
	}

	/**
	 * Matcher to check if a string match a specific pattern
	 *
	 * @param pattern The pattern
	 * @return The corresponding String Matcher
	 */
	public static StringMatcher matchPattern(final String pattern) {
		return new StringMatcher("should match pattern " + pattern) {

			@Override
			public boolean matches(Object item) {
				return ((String)item).matches(pattern);
			}
		};
	}

}
