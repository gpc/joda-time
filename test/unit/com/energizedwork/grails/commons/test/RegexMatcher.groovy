/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
