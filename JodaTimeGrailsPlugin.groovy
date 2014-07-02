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

import grails.plugin.jodatime.JodaDynamicMethods
import grails.plugin.jodatime.binding.DateTimeConverter
import grails.plugin.jodatime.binding.DateTimeStructuredBindingEditor
import grails.plugin.jodatime.binding.DateTimeZoneConverter
import grails.plugin.jodatime.binding.JodaTimePropertyEditorRegistrar
import grails.plugin.jodatime.binding.PeriodConverter
import grails.plugin.jodatime.binding.PeriodStructuredBindingEditor
import grails.plugin.jodatime.converters.JodaConverters

class JodaTimeGrailsPlugin {

	def version = '1.5'
	def grailsVersion = '2.3 > *'
	def dependsOn = [converters: '2.0 > *']

	def title = 'Joda-Time Plugin'
	def description = 'Joda Time integration for Grails'
	def author = 'Rob Fletcher'
	def authorEmail = 'rob@freeside.co'

	def license = 'APACHE'
	def developers = [[name: 'Rob Fletcher', email: 'rob@freeside.co']]
	def documentation = 'http://gpc.github.com/grails-joda-time/'
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPJODATIME']
	def scm = [url: 'https://github.com/gpc/grails-joda-time/']

	def pluginExcludes = [
			'grails-app/controllers/**',
			'grails-app/domain/**',
			'grails-app/i18n/**',
			'web-app/**'
	]

	def doWithSpring = {
		jodaTimePropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)

		DateTimeConverter.SUPPORTED_TYPES.each{ jodaType ->
			"joda${jodaType.simpleName}Converter"(DateTimeConverter) {
				grailsApplication = ref("grailsApplication")
				type = jodaType
			}
		}
		PeriodConverter.SUPPORTED_TYPES.each{ jodaType ->
			"joda${jodaType.simpleName}Converter"(PeriodConverter) {
				type = jodaType
			}
		}
		"jodaDateTimeZoneConverter"(DateTimeZoneConverter)
	}

	def doWithDynamicMethods = { ctx ->
		JodaDynamicMethods.registerDynamicMethods()
		JodaConverters.registerJsonAndXmlMarshallers()
		DateTimeStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
			application.mainContext.grailsWebDataBinder.registerStructuredEditor type, new DateTimeStructuredBindingEditor(type)
		}
		PeriodStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
			application.mainContext.grailsWebDataBinder.registerStructuredEditor type, new PeriodStructuredBindingEditor(type)
		}
	}
}
