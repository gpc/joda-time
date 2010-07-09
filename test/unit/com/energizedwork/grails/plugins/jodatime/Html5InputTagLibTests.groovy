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

package com.energizedwork.grails.plugins.jodatime

import grails.test.TagLibUnitTestCase
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.junit.Ignore

class Html5InputTagLibTests extends TagLibUnitTestCase {

	@Before
	void setUp() {
		super.setUp()

		String.metaClass.encodeAsHTML = {-> HTMLCodec.encode(delegate) }

		def mockGrailsApplication = [config: new ConfigObject()]
		tagLib.metaClass.getGrailsApplication = {-> mockGrailsApplication }
		tagLib.metaClass.getOutput = {-> delegate.out.toString() }

		mockRequest.addPreferredLocale Locale.UK

		def fixedDateTime = new DateTime(2008, 10, 2, 2, 50, 33, 0)
		DateTimeUtils.setCurrentMillisFixed fixedDateTime.getMillis()
	}

	@After
	void tearDown() {
		super.tearDown()

		DateTimeUtils.setCurrentMillisSystem()
	}

	@Test
	void dateFieldRendersHtml5DateInput() {
		tagLib.dateField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="date" name="foo" id="foo" value="" />')
	}

	@Test
	void dateFieldRendersValueInCorrectFormat() {
		tagLib.dateField(name: "foo", value: new DateTime())

		assertThat tagLib.output, containsString('value="2008-10-02"')
	}

	@Test
	void timeFieldRendersHtml5TimeInput() {
		tagLib.timeField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="time" name="foo" id="foo" value="" />')
	}

	@Test
	void timeFieldRendersValueInCorrectFormat() {
		tagLib.timeField(name: "foo", value: new DateTime())

		assertThat tagLib.output, containsString('value="02:50:33.000"')
	}

	@Test
	void datetimeLocalFieldRendersHtml5DatetimeLocalInput() {
		tagLib.datetimeLocalField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="datetime-local" name="foo" id="foo" value="" />')
	}

	@Test
	void datetimeLocalFieldRendersValueInCorrectFormat() {
		tagLib.datetimeLocalField(name: "foo", value: new DateTime())

		assertThat tagLib.output, containsString('value="2008-10-02T02:50:33.000"')
	}

	@Test
	void monthFieldRendersHtml5MonthInput() {
		tagLib.monthField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="month" name="foo" id="foo" value="" />')
	}

	@Test
	void monthFieldRendersValueInCorrectFormat() {
		tagLib.monthField(name: "foo", value: new DateTime())

		assertThat tagLib.output, containsString('value="2008-10"')
	}

	@Test
	void weekFieldRendersHtml5MonthInput() {
		tagLib.weekField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="week" name="foo" id="foo" value="" />')
	}

	@Test
	void weekFieldRendersValueInCorrectFormat() {
		tagLib.weekField(name: "foo", value: new DateTime())

		assertThat tagLib.output, containsString('value="2008-W40"')
	}

	@Test
	void datetimeFieldRendersHtml5MonthInput() {
		tagLib.datetimeField(name: "foo")

		assertThat tagLib.output, equalTo('<input type="datetime" name="foo" id="foo" value="" />')
	}

	@Test
	void datetimeFieldRendersValueInCorrectFormatForUTC() {
		tagLib.datetimeField(name: "foo", value: new DateTime().toLocalDateTime().toDateTime(DateTimeZone.UTC))

		assertThat tagLib.output, containsString('value="2008-10-02T02:50:33.000Z"')
	}

	@Test
	void datetimeFieldRendersValueInCorrectFormatForNonUTC() {
		tagLib.datetimeField(name: "foo", value: new DateTime().toLocalDateTime().toDateTime(DateTimeZone.forOffsetHours(-8)))

		assertThat tagLib.output, containsString('value="2008-10-02T02:50:33.000-08:00"')
	}

	@Test @Ignore
	void datetimeFieldHandlesPartialValues() {
		tagLib.datetimeField(name: "foo", value: new LocalDateTime())

		assertThat tagLib.output, containsString('value="2008-10-02T02:50:33.000Z"')
	}

}
