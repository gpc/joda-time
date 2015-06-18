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

package grails.plugins.jodatime

import grails.plugins.jodatime.converters.JodaConverters
import grails.plugins.Plugin
import grails.plugins.jodatime.binding.DateTimeConverter
import grails.plugins.jodatime.binding.DateTimeStructuredBindingEditor
import grails.plugins.jodatime.binding.DateTimeZoneConverter
import grails.plugins.jodatime.binding.JodaTimePropertyEditorRegistrar
import grails.plugins.jodatime.binding.PeriodConverter
import grails.plugins.jodatime.binding.PeriodStructuredBindingEditor

class JodaTimeGrailsPlugin extends Plugin {

    def version = "2.0.0-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.1 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            'grails-app/controllers/**',
            'grails-app/domain/**',
            'grails-app/i18n/**',
            'src/main/webapp/**'
    ]

    def title = "Joda-Time Plugin" // Headline display name of the plugin
    def author = "Rob Fletcher"
    def authorEmail = "rob@freeside.co"
    def description = '''\
Joda Time integration for Grails
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-joda-time"

    def license = "APACHE"

    def organization = [name: "TO THE NEW Digital", url: "http://www.tothenew.com/"]

    def developers = [
            [name: 'Rob Fletcher', email: 'rob@freeside.co'],
            [name: "Puneet Behl", email: "puneet.behl007@gmail.com"]
    ]

    def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPJODATIME']

    def scm = [url: 'https://github.com/gpc/grails-joda-time/']

    Closure doWithSpring() {
        { ->
            jodaTimePropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)

            DateTimeConverter.SUPPORTED_TYPES.each{ jodaType ->
                "joda${jodaType.simpleName}Converter"(DateTimeConverter) {
                    grailsApplication = grailsApplication
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
    }

    void doWithDynamicMethods() {
        JodaDynamicMethods.registerDynamicMethods()
        JodaConverters.registerJsonAndXmlMarshallers()
        DateTimeStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsApplication.mainContext.grailsWebDataBinder.registerStructuredEditor type, new DateTimeStructuredBindingEditor(type)
        }
        PeriodStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsApplication.mainContext.grailsWebDataBinder.registerStructuredEditor type, new PeriodStructuredBindingEditor(type)
        }
    }

}
