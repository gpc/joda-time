package com.energizedwork.grails.commons.test

import org.hamcrest.TypeSafeMatcher
import org.hamcrest.Description
import java.util.regex.Pattern
import org.hamcrest.Matcher

class RegexMatcher extends TypeSafeMatcher<String> {

	private final Pattern pattern

	static Matcher<String> isMatch(Pattern pattern) {
		return new RegexMatcher(pattern)
	}

	static Matcher<String> isMatch(String regex) {
		return new RegexMatcher(regex)
	}

	RegexMatcher(Pattern pattern) {
		this.pattern = pattern
	}

	RegexMatcher(String regex) {
		this.pattern = ~regex
	}

	boolean matchesSafely(String item) {
		return item =~ pattern
	}

	void describeTo(Description description) {
		description.appendText("a String matching the pattern ").appendValue(pattern)
	}
}
