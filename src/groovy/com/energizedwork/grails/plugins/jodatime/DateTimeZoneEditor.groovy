package com.energizedwork.grails.plugins.jodatime

import java.beans.PropertyEditorSupport
import org.joda.time.DateTimeZone

class DateTimeZoneEditor extends PropertyEditorSupport {

	String getAsText() {
		value?.ID ?: ""
	}

	void setAsText(String text) {
		value = text ? DateTimeZone.forID(text) : null
	}

}