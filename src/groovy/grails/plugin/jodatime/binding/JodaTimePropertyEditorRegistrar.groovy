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
package grails.plugin.jodatime.binding

import org.joda.time.DateTimeZone
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry

class JodaTimePropertyEditorRegistrar implements PropertyEditorRegistrar {

	void registerCustomEditors(PropertyEditorRegistry registry) {
		for (type in DateTimeEditor.SUPPORTED_TYPES) {
			registry.registerCustomEditor type, new StructuredDateTimeEditor(type)
		}

		for (type in PeriodEditor.SUPPORTED_TYPES) {
			registry.registerCustomEditor type, new StructuredPeriodEditor(type)
		}

		registry.registerCustomEditor DateTimeZone, new DateTimeZoneEditor()
	}
}
