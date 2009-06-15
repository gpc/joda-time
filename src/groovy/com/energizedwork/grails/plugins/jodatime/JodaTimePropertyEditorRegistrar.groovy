package com.energizedwork.grails.plugins.jodatime

import com.energizedwork.grails.plugins.jodatime.JodaDateTimeEditor
import com.energizedwork.grails.plugins.jodatime.StructuredJodaDateTimeEditor
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry

class JodaTimePropertyEditorRegistrar implements PropertyEditorRegistrar {

	void registerCustomEditors(PropertyEditorRegistry registry) {
		JodaDateTimeEditor.SUPPORTED_TYPES.each { type ->
			registry.registerCustomEditor(type, new StructuredJodaDateTimeEditor(type))
		}
	}
}
