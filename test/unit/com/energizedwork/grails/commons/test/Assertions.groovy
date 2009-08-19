package com.energizedwork.grails.commons.test

import junit.framework.Assert

class Assertions {

	static void assertMatch(String expectedRegex, actual) {
		def matcher = actual =~ expectedRegex
		if (!matcher) {
			Assert.fail "Expected match of $expectedRegex but found $actual"
		}
	}

	static void assertNoMatch(String expectedRegex, actual) {
		def matcher = actual =~ expectedRegex
		if (matcher) {
			Assert.fail "Expected no match of $expectedRegex but found $actual"
		}
	}

}