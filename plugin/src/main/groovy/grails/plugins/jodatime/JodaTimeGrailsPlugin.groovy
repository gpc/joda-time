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

import grails.plugins.Plugin
import grails.plugins.jodatime.binding.*
import grails.plugins.jodatime.converters.FormattedDateTimeConverter
import grails.plugins.jodatime.converters.JodaConverters
import grails.web.databinding.GrailsWebDataBinder

class JodaTimeGrailsPlugin extends Plugin {

    def grailsVersion = '7.0.0-SNAPSHOT > *'

    def title = 'Grails Joda Time Plugin'
    def description = 'Provides Joda Time databinding integration for Grails'
    def documentation = "http://github.com/grails-plugins/grails-joda-time"

    def license = "APACHE"
    def organization = [name: 'Grails', url: 'https://grails.org']
    def issueManagement = [system: 'Github', url: 'https://github.com/grails-plugins/grails-joda-time/issues']
    def scm = [url: 'https://github.com/grails-plugins/grails-joda-time']

    Closure doWithSpring() {{ ->
        jodaTimePropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)

        DateTimeConverter.SUPPORTED_TYPES.each{ jodaType ->
            "joda${jodaType.simpleName}Converter"(DateTimeConverter) {
                configuration = config
                type = jodaType
            }
        }
        PeriodConverter.SUPPORTED_TYPES.each{ jodaType ->
            "joda${jodaType.simpleName}Converter"(PeriodConverter) {
                type = jodaType
            }
        }
        "jodaDateTimeZoneConverter"(DateTimeZoneConverter)
    }}

    void doWithDynamicMethods() {
        JodaDynamicMethods.registerDynamicMethods()
        JodaConverters.registerJsonAndXmlMarshallers()

        GrailsWebDataBinder grailsWebDataBinder = grailsApplication.mainContext.grailsWebDataBinder
        DateTimeStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsWebDataBinder.registerStructuredEditor type, new DateTimeStructuredBindingEditor(type)
        }
        PeriodStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsWebDataBinder.registerStructuredEditor type, new PeriodStructuredBindingEditor(type)
        }
        FormattedDateTimeConverter.SUPPORTED_TYPES.each { type ->
            grailsWebDataBinder.registerFormattedValueConverter new FormattedDateTimeConverter(type)
        }
    }
}
