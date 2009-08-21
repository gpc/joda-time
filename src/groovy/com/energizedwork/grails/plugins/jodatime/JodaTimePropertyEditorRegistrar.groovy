package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.DateTimeEditor
import com.energizedwork.grails.plugins.jodatime.DateTimeZoneEditor
import com.energizedwork.grails.plugins.jodatime.PeriodEditor
import com.energizedwork.grails.plugins.jodatime.StructuredDateTimeEditor
import com.energizedwork.grails.plugins.jodatime.StructuredPeriodEditor
import org.joda.time.DateTimeZone
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry

class JodaTimePropertyEditorRegistrar implements PropertyEditorRegistrar {

	void registerCustomEditors(PropertyEditorRegistry registry) {
		DateTimeEditor.SUPPORTED_TYPES.each { type ->
			registry.registerCustomEditor type, new StructuredDateTimeEditor(type)
		}

		PeriodEditor.SUPPORTED_TYPES.each { type ->
			registry.registerCustomEditor type, new StructuredPeriodEditor(type)
		}

		registry.registerCustomEditor DateTimeZone, new DateTimeZoneEditor()
	}
}
