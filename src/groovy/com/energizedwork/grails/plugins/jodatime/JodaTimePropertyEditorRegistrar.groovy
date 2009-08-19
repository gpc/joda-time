package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.DateTimeEditor
import com.energizedwork.grails.plugins.jodatime.StructuredDateTimeEditor
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.joda.time.Period

class JodaTimePropertyEditorRegistrar implements PropertyEditorRegistrar {

	void registerCustomEditors(PropertyEditorRegistry registry) {
		DateTimeEditor.SUPPORTED_TYPES.each { type ->
			registry.registerCustomEditor type, new StructuredDateTimeEditor(type)
		}

		PeriodEditor.SUPPORTED_TYPES.each { type ->
			registry.registerCustomEditor type, new StructuredPeriodEditor(type)
		}
	}
}
