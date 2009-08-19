package com.energizedwork.grails.plugins.jodatime

import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.Duration

class StructuredPeriodEditorTests extends GroovyTestCase {

	void testAssembleWorksWithNoFields() {
		def editor = new StructuredPeriodEditor(Period)
		assertEquals new Period(), editor.assemble(Period, [:])
	}

	void testAssembleWorksWithAllFields() {
		def editor = new StructuredPeriodEditor(Period)
		def fields = (0..<PeriodType.standard().size()).collect { PeriodType.standard().getFieldType(it) }
		def values = [:]
		fields.each {
			values[it.name] = "1"
		}
		assertEquals new Period(1, 1, 1, 1, 1, 1, 1, 1), editor.assemble(Period, values)
	}

	void testAssembleDefaultsFieldsToZero() {
		def editor = new StructuredPeriodEditor(Period)
		def values = [years: "1", days: "1", minutes: "1"]
		assertEquals new Period(1, 0, 0, 1, 0, 1, 0, 0), editor.assemble(Period, values)
	}

	void testAssembleHandlesEmptyStrings() {
		def editor = new StructuredPeriodEditor(Period)
		def values = [years: "", days: "1", minutes: "1"]
		assertEquals new Period(0, 0, 0, 1, 0, 1, 0, 0), editor.assemble(Period, values)
	}

	void testAssembleWorksWithNoFieldsForDuration() {
		def editor = new StructuredPeriodEditor(Duration)
		assertEquals Duration.ZERO, editor.assemble(Duration, [:])
	}

	void testEditorDoesNotSupportAllFieldsForDuration() {
		def editor = new StructuredPeriodEditor(Duration)
		// years and months are ignored when binding to Duration
		assertEquals new Period(1, 0, 0, 0).toStandardDuration(), editor.assemble(Duration, [years: 1, hours: 1])
		assertEquals new Period(1, 0, 0, 0).toStandardDuration(), editor.assemble(Duration, [months: 1, hours: 1])
	}

	void testAssembleDefaultsFieldsToZeroForDuration() {
		def editor = new StructuredPeriodEditor(Duration)
		def values = [hours: "1", seconds: "1"]
		assertEquals new Period(1, 0, 1, 0).toStandardDuration(), editor.assemble(Duration, values)
	}

	void testAssembleHandlesEmptyStringsForDuration() {
		def editor = new StructuredPeriodEditor(Duration)
		def values = [hours: "", minutes: "1", seconds: "1"]
		assertEquals new Period(0, 1, 1, 0).toStandardDuration(), editor.assemble(Duration, values)
	}

}