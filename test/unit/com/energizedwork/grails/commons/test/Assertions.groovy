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