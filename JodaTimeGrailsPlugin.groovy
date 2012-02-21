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
import grails.plugin.jodatime.binding.JodaTimePropertyEditorRegistrar
import grails.plugin.jodatime.converters.JodaConverters

class JodaTimeGrailsPlugin {

	def version = '1.4'
	def grailsVersion = '1.3 > *'
	def dependsOn = [converters: '1.3 > *']

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
	}

	def doWithDynamicMethods = { ctx ->
		JodaDynamicMethods.registerDynamicMethods()
		JodaConverters.registerJsonAndXmlMarshallers()
	}
}
